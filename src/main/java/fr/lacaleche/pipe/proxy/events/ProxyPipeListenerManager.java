package fr.lacaleche.pipe.proxy.events;

import fr.lacaleche.core.events.interfaces.IListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;

public interface ProxyPipeListenerManager extends IListenerManager {

    /**
     * TODO
     * */
    void registerProxyListener(IModule module, Object listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    void unregisterProxyListener(Object listener);

    /**
     * TODO
     *
     * @since 1.0.0
     * */
    void unregisterProxyListeners(IModule module);

}
