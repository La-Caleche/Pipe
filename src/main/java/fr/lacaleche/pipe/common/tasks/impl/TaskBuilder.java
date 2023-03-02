package fr.lacaleche.pipe.common.tasks.impl;

import fr.lacaleche.pipe.common.tasks.interfaces.SimpleCallback;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskCallback;

public class TaskBuilder {

    private Task task;

    private int delay;
    private int everyXTick;
    private boolean loop;
    private TaskCallback callback;
    private SimpleCallback stopCallback;

    public TaskBuilder() {
        this.delay = 0;
        this.everyXTick = 1;
        this.loop = false;
        this.callback = (t) -> {};
        this.stopCallback = () -> {};
    }

    public TaskBuilder setTask(Task task) {
        this.task = task;
        return this;
    }

    public TaskBuilder startAfter(int delay) {
        this.delay = delay;
        return this;
    }

    public TaskBuilder loop(boolean loop) {
        return this.loop(loop, 0);
    }

    public TaskBuilder loop(boolean loop, int everyXTick) {
        this.loop = loop;
        this.everyXTick = everyXTick;
        return this;
    }

    public TaskBuilder callback(TaskCallback callback) {
        this.callback = callback;
        return this;
    }

    public TaskBuilder stopCallback(SimpleCallback stopCallback) {
        this.stopCallback = stopCallback;
        return this;
    }

    public Task build() {
        return new TaskImpl(delay, everyXTick, loop, callback, stopCallback);
    }

}
