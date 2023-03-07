package fr.lacaleche.pipe;

import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;

import java.util.List;
import java.util.UUID;

public interface Pipe {

    <T> T getPlugin();

    void setPlugin(Object plugin);

    void registerNewPlugin(Object plugin);

    <T> List<T> getRegisteredPlugins();

    void unregister(Object plugin);

    <T> T getListenerManager();

    /**
     * Get command manager that manager modules commands
     *
     * @since 1.0.0
     *
     * @return a BukkitCommandManager or a BungeeCommandManager depend on api child
     * */
    CommandManager getCommandManager();

    /**
     * TODO
     * */
    void setCommandManager(CommandManager manager);

    /**
     * TODO
     * */
    Locale getDefaultLocale();

    /**
     * TODO
     * */
    Client getClient(UUID uuid);

    /**
     * TODO
     * */
    Client getClientById(int id);


    /**
     * TODO
     * */
    void setShutdownHook(Runnable hook);

    /**
     * TODO
     * */
    Runnable getShutdownHook();

    /**
     * TODO
     * */
    void shutdown();

    /**
     * TODO
     * */
    List<Class<? extends IModule>> getCachedGodModules();

    /**
     * TODO
     * */
    TaskManager getTaskManager();

    /**
     * TODO
     * */
    void setTaskManager(TaskManager taskManager);

    /**
     * TODO
     * */
    TabManager getTabManager();

    /**
     * TODO
     * */
    void setTabManager(TabManager tabManager);

    /**
     * TODO
     * */
    void shutdown(String reason);

    /**
     * TODO
     * */
    long serverTick();

    /**
     * TODO
     * */
    void tick();

    static Pipe get() {
        return PipeImpl.get();
    }

}
