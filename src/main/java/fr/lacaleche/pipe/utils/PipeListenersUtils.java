package fr.lacaleche.pipe.utils;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PipeListenersUtils {

    /**
     * Register a Bukkit event Listener
     *
     * @since 1.0.0
     *
     * @param listener the listener to register
     * */
    public static void registerNewListener(JavaPlugin parent, Listener listener) {
        JavaPlugin server = (JavaPlugin) parent;
        server.getServer().getPluginManager().registerEvents(listener, server);
    }

}
