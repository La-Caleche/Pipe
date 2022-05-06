package fr.lacaleche.pipe.proxy.events;

import fr.lacaleche.core.events.interfaces.IListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;
import net.md_5.bungee.api.plugin.Listener;

public interface ProxyPipeListenerManager extends IListenerManager {

    /**
     * TODO
     * */
    public void registerProxyListener(IModule module, Listener listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    public void unregisterProxyListener(Listener listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    public void unregisterProxyListeners(IModule module);

}
