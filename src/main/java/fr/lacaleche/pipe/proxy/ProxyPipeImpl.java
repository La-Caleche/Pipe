package fr.lacaleche.pipe.proxy;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.AbstractPipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.BukkitPipeImpl;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyPipeImpl extends AbstractPipe implements ProxyPipe {

    public static ProxyPipe instance;

    private ProxyPlugin plugin;

    private final Map<Module, List<TriConsumer<PostLoginEvent, Player, Client>>> joinCallbacks;
    private final Map<Module, List<TriConsumer<ServerConnectedEvent, Player, Client>>> joinServerCallbacks;
    private final Map<Module, List<TriConsumer<DisconnectEvent, Player, Client>>> quitCallbacks;


    public ProxyPipeImpl() {
        super();

        this.joinCallbacks = new HashMap<>();
        this.joinServerCallbacks = new HashMap<>();
        this.quitCallbacks = new HashMap<>();
    }

    public static ProxyPipe get() {
        if (instance == null) {
            instance = new ProxyPipeImpl();
        }
        return instance;
    }


    @Override
    public ProxyPlugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void setPlugin(ProxyPlugin plugin) {
        if (this.plugin != null) {
            throw new RuntimeException("Plugin is already set");
        }
        this.plugin = plugin;
    }

    @Override
    public ProxyPipeListenerManager getListenerManager() {
        return Core.get().getListenerManager();
    }

    @Override
    public void addJoinCallback(Module module, TriConsumer<PostLoginEvent, Player, Client> callback) {
        List<TriConsumer<PostLoginEvent, Player, Client>> moduleCallbacks = this.joinCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.joinCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addJoinServerCallback(Module module, TriConsumer<ServerConnectedEvent, Player, Client> callback) {
        List<TriConsumer<ServerConnectedEvent, Player, Client>> moduleCallbacks = this.joinServerCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.joinServerCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addQuitCallbacks(Module module, TriConsumer<DisconnectEvent, Player, Client> callback) {
        List<TriConsumer<DisconnectEvent, Player, Client>> moduleCallbacks = this.quitCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.quitCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void removeJoinCallbacks(Module module) {
        this.joinCallbacks.remove(module);
    }

    @Override
    public void removeJoinServerCallbacks(Module module) {
        this.joinServerCallbacks.remove(module);
    }

    @Override
    public void removeQuitCallbacks(Module module) {
        this.quitCallbacks.remove(module);
    }

    @Override
    public Map<Module, List<TriConsumer<PostLoginEvent, Player, Client>>> getJoinCallbacks() {
        return this.joinCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<ServerConnectedEvent, Player, Client>>> getJoinServerCallbacks() {
        return this.joinServerCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<DisconnectEvent, Player, Client>>> getQuitCallbacks() {
        return this.quitCallbacks;
    }

    @Override
    public Client getClient(Player player) {
        return this.getClient(player.getUniqueId());
    }
}
