package fr.lacaleche.pipe.bukkit.events;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitPipeListenerManagerImpl extends GlobalListenerManager implements BukkitPipeListenerManager {


    private static final Map<IModule, List<Listener>> bukkitListeners = new HashMap<>();

    @Override
    public void registerBukkitListener(IModule module, Listener listener) {
        if (!(Pipe.getBukkit().getPlugin() instanceof JavaPlugin)) throw new IllegalStateException("Pipe is not on a Bukkit server");

        Plugin plugin = Pipe.getBukkit().getPlugin();
        List<Listener> listeners = bukkitListeners.get(module);

        if (listeners == null)
            listeners = new ArrayList<>();

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);

        listeners.add(listener);
        bukkitListeners.put(module, listeners);
    }

    @Override
    public void unregisterBukkitListener(Listener listener) {
        if (!(Pipe.getBukkit().getPlugin() instanceof JavaPlugin)) throw new IllegalStateException("Pipe is not on a Bukkit server");

        IModule module = this.getModuleFor(listener);
        if (module != null) {
            List<Listener> listeners = bukkitListeners.get(module);
            if (listeners != null) {
                listeners.remove(listener);
            }

            HandlerList.unregisterAll(listener);

            bukkitListeners.put(module, listeners);
        }
    }

    @Override
    public void unregisterBukkitListeners(IModule module) {
        if (!(Pipe.getBukkit().getPlugin() instanceof JavaPlugin)) throw new IllegalStateException("Pipe is not on a Bukkit server");

        List<Listener> listeners = this.getCustomListeners(module);
        if (listeners.isEmpty()) return;

        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }

        bukkitListeners.remove(module).clear();
    }

    @Override
    public void registerBoth(IModule module, BukkitPipeListener listener) {
        this.registerBukkitListener(module, listener);
        this.registerCustomListener(module, listener);
    }

    private List<Listener> getCustomListeners(IModule module) {
        List<Listener> listeners = bukkitListeners.get(module);
        if (listeners == null)
            listeners = new ArrayList<>();
        return listeners;
    }

    private IModule getModuleFor(Listener listener) {
        IModule module = null;
        for (IModule m : bukkitListeners.keySet()) {
            if (bukkitListeners.get(m).contains(listener)) {
                module = m;
                break;
            }
        }
        return module;
    }

}
