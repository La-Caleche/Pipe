package fr.lacaleche.pipe.common.tasks.interfaces;

public interface Task {

    boolean isLoop();

    void stop();

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
