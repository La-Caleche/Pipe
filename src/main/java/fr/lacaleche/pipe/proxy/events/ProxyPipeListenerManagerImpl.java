package fr.lacaleche.pipe.proxy.events;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.proxy.ProxyPipe;
import fr.lacaleche.pipe.proxy.ProxyPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyPipeListenerManagerImpl extends GlobalListenerManager implements ProxyPipeListenerManager {

    private static final Map<IModule, List<Object>> proxyListeners = new HashMap<>();

    @Override
    public void registerProxyListener(IModule module, Object listener) {
        ProxyPipe pipe = Pipe.getProxy();
        ProxyPlugin plugin = pipe.getPlugin();
        List<Object> listeners = proxyListeners.get(module);

        if (listeners == null)
            listeners = new ArrayList<>();

        plugin.getServer().getEventManager().register(plugin, listener);

        listeners.add(listener);
        proxyListeners.put(module, listeners);
    }

    @Override
    public void unregisterProxyListener(Object listener) {
        ProxyPipe pipe = Pipe.getProxy();
        ProxyPlugin plugin = pipe.getPlugin();
        IModule module = this.getModuleFor(listener);
        if (module != null) {
            List<Object> listeners = proxyListeners.get(module);
            if (listeners != null) {
                listeners.remove(listener);
            }

            plugin.getServer().getEventManager().unregisterListener(plugin, listener);

            proxyListeners.put(module, listeners);
        }
    }

    @Override
    public void unregisterProxyListeners(IModule module) {
        ProxyPipe pipe = Pipe.getProxy();
        ProxyPlugin plugin = pipe.getPlugin();
        List<Object> listeners = this.getCustomListeners(module);
        if (listeners.isEmpty()) return;

        for (Object listener : listeners) {
            plugin.getServer().getEventManager().unregisterListener(plugin, listener);
        }

        proxyListeners.remove(module).clear();
    }

    private List<Object> getCustomListeners(IModule module) {
        List<Object> listeners = proxyListeners.get(module);
        if (listeners == null)
            listeners = new ArrayList<>();
        return listeners;
    }

    private IModule getModuleFor(Object listener) {
        IModule module = null;
        for (IModule m : proxyListeners.keySet()) {
            if (proxyListeners.get(m).contains(listener)) {
                module = m;
                break;
            }
        }
        return module;
    }

}
