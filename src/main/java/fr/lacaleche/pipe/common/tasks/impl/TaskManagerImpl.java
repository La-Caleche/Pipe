package fr.lacaleche.pipe.common.tasks.impl;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.tasks.events.UpdateTickEvent;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;

import java.util.*;

public class TaskManagerImpl implements fr.lacaleche.pipe.common.tasks.interfaces.TaskManager {

    private boolean running;

    private Map<String, Task> callbacks;
    protected final List<String> done;

    private Map<String, Task> nextCallbacks;

    public TaskManagerImpl() {
        this.callbacks = new HashMap<>();
        this.nextCallbacks = new HashMap<>();
        this.done = new ArrayList<>();
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
        this.nextCallbacks.forEach((name, task) -> {
            task.startNow();
            this.callbacks.put(name, task);
        });
        this.nextCallbacks.clear();

        getCallbacks().forEach((name, task) -> {
            task.tick(event.getTick());
            if (!task.canBeExecuted()) return;

            task.run();

            if (!task.isLoop() && !task.retry()) this.done.add(name);
        });

        this.done.forEach(this.callbacks::remove);
        this.done.clear();
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
    public Task newTask(TaskBuilder taskBuilder) {
        return this.newTask(UUID.randomUUID().toString(), taskBuilder);
    }

    @Override
    public Task newTask(String name, TaskBuilder taskBuilder) {
        if (this.getCallbacks().containsKey(name) || this.nextCallbacks.containsKey(name)) return null;
        Task task = taskBuilder.build();
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
