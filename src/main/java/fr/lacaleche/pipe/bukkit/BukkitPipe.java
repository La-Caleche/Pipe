package fr.lacaleche.pipe.bukkit;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.clients.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public interface BukkitPipe extends Pipe {

    Plugin getPlugin();

    void setPlugin(Plugin plugin);

    void registerNewPlugin(Plugin plugin);

    List<Plugin> getRegisteredPlugins();

    void unregister(Plugin plugin);

    BukkitPipeListenerManager getListenerManager();

    /**
     * TODO
     */
    TabManager getTabManager();

    /**
     * TODO
     */
    List<Class<? extends IModule>> getCachedGodModules();

    /**
     * TODO
     */
    void setTabManager(TabManager tabManager);

    /**
     * TODO
     */
    void addJoinCallback(Module module, TriConsumer<PlayerJoinEvent, Player, Client> callback);

    /**
     * TODO
     */
    void addQuitCallbacks(Module module, TriConsumer<PlayerQuitEvent, Player, Client> callback);

    /**
     * TODO
     */
    void removeJoinCallbacks(Module module);

    /**
     * TODO
     */
    void removeQuitCallbacks(Module module);

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<PlayerJoinEvent, Player, Client>>> getJoinCallbacks();

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<PlayerQuitEvent, Player, Client>>> getQuitCallbacks();

}
