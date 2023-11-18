package fr.lacaleche.pipe.common.tasks.impl;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.tasks.events.UpdateTickEvent;
import fr.lacaleche.pipe.common.tasks.interfaces.BuilderPredicate;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManagerImpl implements fr.lacaleche.pipe.common.tasks.interfaces.TaskManager {

    private boolean running;

    private final Map<String, Task> callbacks;

    private final Map<String, Task> nextCallbacks;

    public TaskManagerImpl() {
        this.callbacks = new ConcurrentHashMap<>();
        this.nextCallbacks = new ConcurrentHashMap<>();
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public void update(UpdateTickEvent event) {
        synchronized (this.nextCallbacks) {
            for (Map.Entry<String, Task> entry : new HashMap<>(nextCallbacks).entrySet()) {
                entry.getValue().startNow();
                callbacks.put(entry.getKey(), entry.getValue());
            }
            nextCallbacks.clear();
        }

        List<Map.Entry<String, Task>> toDo = new ArrayList<>(callbacks.entrySet());
        if (toDo.isEmpty()) return;

        List<String> toRemove = new ArrayList<>();
        toDo.forEach(entry -> {
            String name = entry.getKey();
            Task task = entry.getValue();

            task.tick(event.getTick());
            if (!task.canBeExecuted()) return ;

            try {
                task.run();
            } catch (RuntimeException exception) {
                task.crash(exception);
                toRemove.add(name);
                return ;
            }

            if (!task.isLoop() && !task.retry())
                toRemove.add(name);
        });
        toRemove.forEach(callbacks::remove);
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public Map<String, Task> getCallbacks() {
        return callbacks;
    }

    @Override
    public Task newTask(BuilderPredicate builderPredicate) {
        return this.newTask(UUID.randomUUID().toString(), builderPredicate.build(new TaskBuilder()));
    }

    @Override
    public Task newTask(TaskBuilder taskBuilder) {
        return this.newTask(UUID.randomUUID().toString(), taskBuilder);
    }

    @Override
    public synchronized Task newTask(String name, TaskBuilder taskBuilder) {
        if (this.getCallbacks().containsKey(name) || this.nextCallbacks.containsKey(name)) return null;
        Task task = taskBuilder.build();

        if (task.zeroTickExecution()) {
            task.run();
            return task;
        }

        this.nextCallbacks.put(name, task);
        return task;
    }

    @Override
    public boolean exist(String name) {
        return getCallbacks().containsKey(name) || this.nextCallbacks.containsKey(name);
    }

    @Override
    public Task get(String name) {
        return getCallbacks().getOrDefault(name, null);
    }
}
