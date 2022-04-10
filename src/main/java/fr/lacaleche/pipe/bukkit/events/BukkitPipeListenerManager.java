package fr.lacaleche.pipe.bukkit.events;

import fr.lacaleche.core.modules.interfaces.IModule;
import org.bukkit.event.Listener;

public interface BukkitPipeListenerManager {

    /**
     * TODO
     * */
    public void registerBukkitListener(IModule module, Listener listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    public void unregisterBukkitListener(Listener listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    public void unregisterBukkitListeners(IModule module);

}
