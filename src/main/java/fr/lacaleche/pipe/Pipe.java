package fr.lacaleche.pipe;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    /**
     * TODO
     * */
    void addJoinCallback(Module module, TriConsumer<PlayerJoinEvent, Player, Client> callback);

    /**
     * TODO
     * */
    void addQuitCallbacks(Module module, TriConsumer<PlayerQuitEvent, Player, Client> callback);

    /**
     * TODO
     * */
    void removeJoinCallbacks(Module module);

    /**
     * TODO
     * */
    void removeQuitCallbacks(Module module);

    /**
     * TODO
     * */
    Map<Module, List<TriConsumer<PlayerJoinEvent, Player, Client>>> getJoinCallbacks();

    /**
     * TODO
     * */
    Map<Module, List<TriConsumer<PlayerQuitEvent, Player, Client>>> getQuitCallbacks();

    /**
     * TODO
     * */
    PipeText text();

    static Pipe get() {
        return PipeImpl.get();
    }

}
