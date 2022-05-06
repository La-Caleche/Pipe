package fr.lacaleche.pipe.proxy.events;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyPipeListenerManagerImpl extends GlobalListenerManager implements ProxyPipeListenerManager {

    private static final Map<IModule, List<Listener>> proxyListeners = new HashMap<>();

    @Override
    public void registerProxyListener(IModule module, Listener listener) {
        Pipe pipe = Pipe.get();
        Plugin plugin = pipe.getPlugin();
        List<Listener> listeners = proxyListeners.get(module);

        if (listeners == null)
            listeners = new ArrayList<>();

        plugin.getProxy().getPluginManager().registerListener(plugin, listener);

        listeners.add(listener);
        proxyListeners.put(module, listeners);
    }

    @Override
    public void unregisterProxyListener(Listener listener) {
        Pipe pipe = Pipe.get();
        Plugin plugin = pipe.getPlugin();
        IModule module = this.getModuleFor(listener);
        if (module != null) {
            List<Listener> listeners = proxyListeners.get(module);
            if (listeners != null) {
                listeners.remove(listener);
            }

            plugin.getProxy().getPluginManager().unregisterListener(listener);

            proxyListeners.put(module, listeners);
        }
    }

    @Override
    public void unregisterProxyListeners(IModule module) {
        Pipe pipe = Pipe.get();
        Plugin plugin = pipe.getPlugin();
        List<Listener> listeners = this.getCustomListeners(module);
        if (listeners.isEmpty()) return;

        for (Listener listener : listeners) {
            plugin.getProxy().getPluginManager().unregisterListener(listener);
        }

        proxyListeners.remove(module).clear();
    }

    private List<Listener> getCustomListeners(IModule module) {
        List<Listener> listeners = proxyListeners.get(module);
        if (listeners == null)
            listeners = new ArrayList<>();
        return listeners;
    }

    private IModule getModuleFor(Listener listener) {
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
