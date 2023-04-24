package fr.lacaleche.pipe;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.BukkitPipeImpl;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;
import fr.lacaleche.pipe.proxy.ProxyPipe;
import fr.lacaleche.pipe.proxy.ProxyPipeImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Pipe {

    /**
     * Get command manager that manager modules commands
     *
     * @return a BukkitCommandManager or a BungeeCommandManager depend on api child
     * @since 1.0.0
     */
    CommandManager getCommandManager();

    /**
     * TODO
     */
    void setCommandManager(CommandManager manager);

    /**
     * TODO
     */
    Locale getDefaultLocale();

    /**
     * TODO
     */
    Client getClient(UUID uuid);

    /**
     * TODO
     */
    Client getClientById(int id);

    /**
     * TODO
     */
    void setShutdownHook(Runnable hook);

    /**
     * TODO
     */
    Runnable getShutdownHook();

    /**
     * TODO
     */
    void shutdown();

    /**
     * TODO
     */
    TaskManager getTaskManager();

    /**
     * TODO
     */
    void setTaskManager(TaskManager taskManager);

    /**
     * TODO
     */
    void shutdown(String reason);

    /**
     * TODO
     */
    long serverTick();

    /**
     * TODO
     */
    void tick();

    /**
     * TODO
     */
    PipeText text();

    /**
     * TODO
     */
    GlobalListenerManager globalListenerManager();

    static BukkitPipe getBukkit() {
        return BukkitPipeImpl.get();
    }

    static ProxyPipe getProxy() {
        return ProxyPipeImpl.get();
    }

    static Pipe get() {
        return AbstractPipe.get();
    }

}
