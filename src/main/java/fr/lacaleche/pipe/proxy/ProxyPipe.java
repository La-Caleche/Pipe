package fr.lacaleche.pipe.proxy;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;

import java.util.List;
import java.util.Map;

public interface ProxyPipe extends Pipe {

    ProxyPlugin getPlugin();

    void setPlugin(ProxyPlugin plugin);

    ProxyPipeListenerManager getListenerManager();

    /**
     * TODO
     */
    void addJoinCallback(Module module, TriConsumer<PostLoginEvent, Player, Client> callback);

    /**
     * TODO
     */
    void addJoinServerCallback(Module module, TriConsumer<ServerConnectedEvent, Player, Client> callback);

    /**
     * TODO
     */
    void addQuitCallbacks(Module module, TriConsumer<DisconnectEvent, Player, Client> callback);

    /**
     * TODO
     */
    void removeJoinCallbacks(Module module);

    /**
     * TODO
     */
    void removeJoinServerCallbacks(Module module);

    /**
     * TODO
     */
    void removeQuitCallbacks(Module module);

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<PostLoginEvent, Player, Client>>> getJoinCallbacks();

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<ServerConnectedEvent, Player, Client>>> getJoinServerCallbacks();

    /**
     * TODO
     */
    Map<Module, List<TriConsumer<DisconnectEvent, Player, Client>>> getQuitCallbacks();

}
