package fr.lacaleche.pipe.common.tasks.impl;

import fr.lacaleche.pipe.common.tasks.interfaces.ErrorCallback;
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
    private ErrorCallback errorCallback;

    private boolean async;
    private boolean zeroTickExecution;

    public TaskBuilder() {
        this.delay = 0;
        this.everyXTick = 1;
        this.loop = false;
        this.async = false;
        this.zeroTickExecution = false;
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

    public TaskBuilder run(TaskCallback callback) {
        this.callback = callback;
        return this;
    }

    public TaskBuilder stopFunction(SimpleCallback stopCallback) {
        this.stopCallback = stopCallback;
        return this;
    }

    public TaskBuilder async(boolean async) {
        this.async = async;
        return this;
    }

    public TaskBuilder zeroTickExecution(boolean zeroTickExecution) {
        this.zeroTickExecution = zeroTickExecution;
        return this;
    }

    public TaskBuilder error(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
        return this;
    }

    public Task build() {
        return new TaskImpl(delay, everyXTick, loop, async, zeroTickExecution, callback, stopCallback, errorCallback);
    }

}
