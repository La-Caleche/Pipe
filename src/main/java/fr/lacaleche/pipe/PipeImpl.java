package fr.lacaleche.pipe;

import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;

import java.util.ArrayList;
import java.util.List;

public class PipeImpl<T> implements Pipe<T> {

    public static Pipe instance;
    private T plugin;
    private CommandManager commandManager;

    private List<T> plugins;

    public PipeImpl() {
        this.plugins = new ArrayList<>();
    }

    public static Pipe get() {
        if (instance == null) {
            instance = new PipeImpl<>();
        }
        return instance;
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public void setPlugin(T plugin) {
        if (this.plugin != null) {
            throw new RuntimeException("Plugin is already set");
        }
        this.plugin = plugin;
    }

    @Override
    public void registerNewPlugin(T plugin) {
        plugins.add(plugin);
    }

    @Override
    public List<T> getRegisteredPlugins() {
        return this.plugins;
    }

    @Override
    public void unregister(T plugin) {
        if (this.plugin == plugin) {
            this.plugin = null;
        }

        plugins.remove(plugin);
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public BukkitPipeListenerManager getListenerManager() {
        if (!(CalecheCore.get().getListenerManager() instanceof BukkitPipeListenerManager)) throw new RuntimeException("Listener manager is not a PipeListenerManager");

        return (BukkitPipeListenerManager) CalecheCore.get().getListenerManager();
    }

}
