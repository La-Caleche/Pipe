package fr.lacaleche.pipe;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.utils.Callback;
import fr.lacaleche.pipe.common.CommonPipe;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.interfaces.PipeCommandManager;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public interface Pipe {

    /**
     * Get command manager that manager modules commands
     *
     * @return a Cloud 2 Command Manager
     * @since 1.0.0
     */
    PipeCommandManager<?> getCommandManager();

    /**
     * TODO
     */
    <C> void setCommandManager(PipeCommandManager<C> commandManager);

    /**
     * TODO
     */
    Locale getDefaultLocale();

    /**
     * TODO
     */
    @NonNull <C> Locale getLocale(C recipient);

    /**
     * TODO
     */
    @Nullable <C> Client getClient(C recipient);

    /**
     * TODO
     */
    Client getClient(UUID uuid);

    /**
     * TODO
     */
    Client getClientById(int id);

    /**
     * TODO
     */
    void setShutdownHook(Runnable hook);

    /**
     * TODO
     */
    Runnable getShutdownHook();

    /**
     * TODO
     */
    void shutdown();

    /**
     * TODO
     */
    TaskManager getTaskManager();

    /**
     * TODO
     */
    void setTaskManager(TaskManager taskManager);

    /**
     * TODO
     */
    void shutdown(String reason);

    /**
     * TODO
     */
    long serverTick();

    /**
     * TODO
     */
    void tick();

    /**
     * TODO
     */
    PipeText text();

    /**
     * TODO
     */
    GlobalListenerManager globalListenerManager();

    /**
     * TODO
     */
    Class<? extends ClientImpl> getClientClass();

    /**
     * TODO
     */
    Client instanciateClient(UUID uuid, String username);

    /**
     * TODO
     */
    void setClientClass(Class<? extends ClientImpl> clientClass);

    static Task async(Callback<Task> callback) {
        return get().getTaskManager().newTask(builder -> builder.run(callback::done).async(true));
    }

    static Task sync(Callback<Task> callback) {
        return get().getTaskManager().newTask(builder -> builder.run(callback::done));
    }

    static Task asyncZeroTick(Callback<Task> callback) {
        return get().getTaskManager().newTask(builder -> builder.run(callback::done).async(true).zeroTickExecution(true));
    }

    static Class<? extends ClientImpl> clientClass() {
        return get().getClientClass();
    }

    static Pipe getCommon() {
        return CommonPipe.get();
    }

    static Pipe get() {
        return AbstractPipe.get();
    }

}
