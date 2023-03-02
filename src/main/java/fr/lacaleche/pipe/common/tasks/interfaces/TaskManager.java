package fr.lacaleche.pipe.common.tasks.interfaces;

import fr.lacaleche.pipe.common.tasks.events.UpdateTickEvent;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;

import java.util.Map;

public interface TaskManager {

    void start();

    void stop();

    void update(UpdateTickEvent event);

    boolean isRunning();

    Task newTask(TaskBuilder taskBuilder);

    Task newTask(String name, TaskBuilder taskBuilder);

    Task get(String name);

    boolean exist(String name);

    Map<String, Task> getCallbacks();

}
