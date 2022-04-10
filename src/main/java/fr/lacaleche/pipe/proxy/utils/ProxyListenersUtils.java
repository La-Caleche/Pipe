package fr.lacaleche.pipe.proxy.utils;

import fr.lacaleche.pipe.Pipe;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyListenersUtils {

    /**
     * Register a Bungee event Listener
     *
     * @since 2.0.0
     *
     * @param listener the listener to register
     * */
    public static void registerNewListener(Listener listener) {
        Pipe<Plugin> pipe = Pipe.get();
        Plugin server = pipe.getPlugin();
        server.getProxy().getPluginManager().registerListener(server, listener);
    }

}
