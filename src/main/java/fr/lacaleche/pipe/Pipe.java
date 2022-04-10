package fr.lacaleche.pipe;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;

import java.util.List;

public interface Pipe<T> {

    public T getPlugin();

    public void setPlugin(T plugin);

    public void registerNewPlugin(T plugin);

    public List<T> getRegisteredPlugins();

    public void unregister(T plugin);

    public BukkitPipeListenerManager getListenerManager();

    /**
     * Get command manager that manager modules commands
     *
     * @since 1.0.0
     *
     * @return a BukkitCommandManager or a BungeeCommandManager depend on api child
     * */
    CommandManager getCommandManager();

    /**
     * TODO
     * */
    void setCommandManager(CommandManager manager);

    public static Pipe get() {
        return PipeImpl.get();
    }

}
