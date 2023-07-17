package fr.lacaleche.pipe.common.tasks.impl;

import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import fr.lacaleche.pipe.common.tasks.interfaces.SimpleCallback;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskCallback;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TaskImpl implements Task {

    private final UUID uuid;

    private final int delay;
    private int countdown;

    private final int everyXTick;
    private final TaskCallback callback;
    private final SimpleCallback stopCallback;
    private boolean loop;
    private boolean retry;
    private boolean async;
    private boolean zeroTickExecution;

    private int taskTick;
    private int externalTick;
    private long startedAt;
    private long startedAtTick;
    private boolean stopped;

    public TaskImpl(int delay, int everyXTick, boolean loop, boolean async, boolean zeroTickExecution, TaskCallback callback, SimpleCallback stopCallback) {
        this.uuid = UUID.randomUUID();

        this.delay = delay;
        this.countdown = delay;

        this.everyXTick = everyXTick;
        this.loop = loop;
        this.async = async;
        this.zeroTickExecution = zeroTickExecution;
        this.callback = callback;
        this.stopCallback = stopCallback;

        this.taskTick = -1;

        this.startedAt = -1;
        this.startedAtTick = -1;

        this.retry = false;
        this.stopped = false;
    }

    @Override
    public UUID uuid() {
        return this.uuid;
    }

    @Override
    public boolean isLoop() {
        return loop;
    }

    @Override
    public boolean runAsync() {
        return async;
    }

    @Override
    public boolean zeroTickExecution() {
        return zeroTickExecution;
    }

    @Override
    public void stop() {
        this.stopped = true;
        this.loop = false;
        getStopCallback().execute();
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public int everyXTick() {
        return this.everyXTick;
    }

    @Override
    public long elapsed() {
        return System.currentTimeMillis() - startedAt;
    }

    @Override
    public long elapsedTick() {
        return Pipe.get().serverTick() - startedAtTick;
    }

    @Override
    public long startedAt() {
        return startedAt;
    }

    @Override
    public long startedAtTick() {
        return this.startedAtTick;
    }

    @Override
    public int modStartedTick() {
        return (int) (this.startedAtTick % 20);
    }

    @Override
    public void tick(int tick) {
        if (this.countdown > 0) {
            if (this.countdown == 1) this.startNow();
            this.countdown--;
            return;
        }

        this.externalTick = tick;

        if (this.taskTick == 19) this.taskTick = 0;
        else this.taskTick++;
    }

    @Override
    public void run() {
        if (async) CompletableFuture.runAsync(() -> {
            try {
                this.getCallback().execute(this);
            } catch (Exception exception) {
                // Because of async execution, we need to catch the exception here
                SentryAPIImpl.getInstance().captureException(exception);
            }
        });
        else this.getCallback().execute(this);
    }

    @Override
    public int taskTick() {
        return this.taskTick;
    }

    @Override
    public int externalTick() {
        return this.externalTick;
    }

    @Override
    public boolean canBeExecuted() {
        if (this.stopped) return false;
        if (this.countdown > 0) return false;
        if (!this.loop) return true;

        return this.everyXTick == 0 || this.taskTick % this.everyXTick == 0;
    }

    @Override
    public void startNow() {
        if (this.startedAt != -1 || this.startedAtTick != -1)
            return;

        this.startedAt = System.currentTimeMillis();
        this.startedAtTick = Pipe.get().serverTick();
    }

    @Override
    public void retryIn(int tick) {
        this.retry = true;
        this.countdown = tick;
    }

    @Override
    public boolean retry() {
        return this.retry;
    }

    @Override
    public TaskCallback getCallback() {
        return callback;
    }

    @Override
    public SimpleCallback getStopCallback() {
        return stopCallback;
    }
}
