package fr.lacaleche.pipe.common.tasks.interfaces;

import java.util.UUID;

public interface Task {

    UUID uuid();

    boolean isLoop();

    boolean runAsync();

    /**
     * @return true if the task is executed immediately without ticking or loop.
     * */
    boolean zeroTickExecution();

    void stop();

    void run();

    int getDelay();

    int everyXTick();

    long elapsed();

    long elapsedTick();

    long startedAt();

    long startedAtTick();

    int modStartedTick();

    void tick(int tick);

    int taskTick();

    int externalTick();

    void startNow();

    void retryIn(int tick);

    boolean retry();

    boolean canBeExecuted();

    TaskCallback getCallback();

    SimpleCallback getStopCallback();

}
