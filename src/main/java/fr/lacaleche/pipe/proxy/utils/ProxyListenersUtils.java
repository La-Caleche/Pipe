package fr.lacaleche.pipe.proxy.utils;

import com.velocitypowered.api.proxy.ProxyServer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.proxy.ProxyPlugin;

public class ProxyListenersUtils {

    /**
     * Register a Bungee event Listener
     *
     * @since 2.0.0
     *
     * @param listener the listener to register
     * */
    public static void registerNewListener(Object listener) {
        Pipe pipe = Pipe.get();
        ProxyServer server = pipe.<ProxyPlugin>getPlugin().getServer();
        server.getEventManager().register(server, listener);
    }

}
