package fr.lacaleche.pipe.bukkit.events;

import fr.lacaleche.core.events.interfaces.IListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;
import org.bukkit.event.Listener;

public interface BukkitPipeListenerManager extends IListenerManager {

    /**
     * TODO
     * */
    void registerBukkitListener(IModule module, Listener listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    void unregisterBukkitListener(Listener listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    void unregisterBukkitListeners(IModule module);

    void registerBoth(IModule module, BukkitPipeListener listener);

}
