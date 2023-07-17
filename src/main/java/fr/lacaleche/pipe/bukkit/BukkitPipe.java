package fr.lacaleche.pipe.bukkit;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.chat.renderers.AbstractRenderer;
import fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces.PersistentsBlocksManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.clients.Client;
import io.papermc.paper.chat.ChatRenderer;
import org.bukkit.Location;
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

    void registerNewHandler(Plugin plugin, IModuleHandler handler);

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

    Map<Plugin, IModuleHandler> getHandlers();

    /**
     * TODO
     */
    void setChatRenderer(Class<? extends ChatRenderer> renderer);

    /**
     * TODO
     */
    Class<? extends ChatRenderer> getChatRenderer();

    /**
     * TODO
     */
    Client getClient(Player player);

    /**
     * TODO
     */
    Location getLastLocation(Client client);

    /**
     * TODO
     */
    void setLastLocation(Client client, Location location);

    /**
     * TODO
     */
    PersistentsBlocksManager getPersistentsBlocksManager();

    /**
     * TODO
     */
    void setPersistentsBlocksManager(PersistentsBlocksManager persistentsBlocksManager);

}
