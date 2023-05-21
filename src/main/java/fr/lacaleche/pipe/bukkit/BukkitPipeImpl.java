package fr.lacaleche.pipe.bukkit;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.AbstractPipe;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.chat.renderers.AbstractRenderer;
import fr.lacaleche.pipe.bukkit.modules.chat.renderers.PipeRenderer;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.clients.Client;
import io.papermc.paper.chat.ChatRenderer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitPipeImpl extends AbstractPipe implements BukkitPipe {

    public static BukkitPipe instance;

    private Plugin plugin;
    private final List<Plugin> plugins;

    private TabManager tabManager;

    private final Map<Module, List<TriConsumer<PlayerJoinEvent, Player, Client>>> joinCallbacks;
    private final Map<Module, List<TriConsumer<PlayerQuitEvent, Player, Client>>> quitCallbacks;
    private final List<Class<? extends IModule>> cachedGodModules;

    private Class<? extends ChatRenderer> chatRenderer;

    public BukkitPipeImpl() {
        super();

        this.plugins = new ArrayList<>();

        this.joinCallbacks = new HashMap<>();
        this.quitCallbacks = new HashMap<>();

        this.cachedGodModules = new ArrayList<>();

        this.chatRenderer = PipeRenderer.class;
    }

    public static BukkitPipe get() {
        if (instance == null) {
            instance = new BukkitPipeImpl();
        }
        return instance;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void setPlugin(Plugin plugin) {
        if (this.plugin != null) {
            throw new RuntimeException("Plugin is already set");
        }
        this.plugin = plugin;
    }

    @Override
    public void registerNewPlugin(Plugin plugin) {
        plugins.add(plugin);
    }

    @Override
    public List<Plugin> getRegisteredPlugins() {
        return this.plugins;
    }

    @Override
    public void unregister(Plugin plugin) {
        if (this.plugin == plugin) {
            this.plugin = null;
        }

        plugins.remove(plugin);
    }

    @Override
    public TabManager getTabManager() {
        return this.tabManager;
    }

    @Override
    public void setTabManager(TabManager tabManager) {
        this.tabManager = tabManager;
    }

    @Override
    public BukkitPipeListenerManager getListenerManager() {
        return Core.get().getListenerManager();
    }

    @Override
    public List<Class<? extends IModule>> getCachedGodModules() {
        return cachedGodModules;
    }

    @Override
    public void addJoinCallback(Module module, TriConsumer<PlayerJoinEvent, Player, Client> callback) {
        List<TriConsumer<PlayerJoinEvent, Player, Client>> moduleCallbacks = this.joinCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.joinCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addQuitCallbacks(Module module, TriConsumer<PlayerQuitEvent, Player, Client> callback) {
        List<TriConsumer<PlayerQuitEvent, Player, Client>> moduleCallbacks = this.quitCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.quitCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public Class<? extends ChatRenderer> getChatRenderer() {
        return chatRenderer;
    }

    @Override
    public void setChatRenderer(Class<? extends ChatRenderer> chatRenderer) {
        this.chatRenderer = chatRenderer;
    }

    @Override
    public void removeJoinCallbacks(Module module) {
        this.joinCallbacks.remove(module);
    }

    @Override
    public void removeQuitCallbacks(Module module) {
        this.quitCallbacks.remove(module);
    }

    @Override
    public Client getClient(Player player) {
        return this.getClient(player.getUniqueId());
    }

    @Override
    public Map<Module, List<TriConsumer<PlayerJoinEvent, Player, Client>>> getJoinCallbacks() {
        return joinCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<PlayerQuitEvent, Player, Client>>> getQuitCallbacks() {
        return quitCallbacks;
    }

}
