package fr.lacaleche.pipe;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.Callback;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.BukkitPipeImpl;
import fr.lacaleche.pipe.common.CommonPipe;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;
import fr.lacaleche.pipe.proxy.ProxyPipe;
import fr.lacaleche.pipe.proxy.ProxyPipeImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public interface Pipe {

    /**
     * Get command manager that manager modules commands
     *
     * @return a BukkitCommandManager or a BungeeCommandManager depend on api child
     * @since 1.0.0
     */
    CommandManager getCommandManager();

    /**
     * TODO
     */
    void setCommandManager(CommandManager manager);

    /**
     * TODO
     */
    Locale getDefaultLocale();

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

    static BukkitPipe getBukkit() {
        return BukkitPipeImpl.get();
    }

    static ProxyPipe getProxy() {
        return ProxyPipeImpl.get();
    }

    static Pipe getCommon() {
        return CommonPipe.get();
    }

    static Pipe get() {
        return AbstractPipe.get();
    }

}
