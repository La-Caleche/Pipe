package fr.lacaleche.pipe;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.adventure.PipeTextImpl;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PipeImpl implements Pipe {

    public static Pipe instance;
    private Object plugin;
    private CommandManager commandManager;
    private Runnable shutdownHook;
    private List<Class<? extends IModule>> cachedGodModules;
    private TaskManager taskManager;
    private TabManager tabManager;

    private final List<Object> plugins;
    private final PipeText pipeText;
    private final Map<Module, List<TriConsumer<PlayerJoinEvent, Player, Client>>> joinCallbacks;
    private final Map<Module, List<TriConsumer<PlayerQuitEvent, Player, Client>>> quitCallbacks;

    private long serverTick;

    public PipeImpl() {
        this.plugins = new ArrayList<>();
        this.pipeText = new PipeTextImpl();
        this.cachedGodModules = new ArrayList<>();

        this.joinCallbacks = new HashMap<>();
        this.quitCallbacks = new HashMap<>();

        this.serverTick = 0;
    }

    public static Pipe get() {
        if (instance == null) {
            instance = new PipeImpl();
        }
        return instance;
    }

    @Override
    public <T> T getPlugin() {
        return (T) plugin;
    }

    @Override
    public void setPlugin(Object plugin) {
        if (this.plugin != null) {
            throw new RuntimeException("Plugin is already set");
        }
        this.plugin = plugin;
    }

    @Override
    public void registerNewPlugin(Object plugin) {
        plugins.add(plugin);
    }

    @Override
    public <T> List<T> getRegisteredPlugins() {
        return (List<T>) this.plugins;
    }

    @Override
    public void unregister(Object plugin) {
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
    public <T> T getListenerManager() {
        return Core.get().getListenerManager();
    }

    @Override
    public Locale getDefaultLocale() {
        return new ModelFilter<LocaleImpl>().model(LocaleImpl.class).cache(LocaleImpl::isDefault).getOne();
    }

    @Override
    public Client getClient(UUID uuid) {
        return new ModelFilter<ClientImpl>().model(ClientImpl.class).cache((client) -> client.getUUID().equals(uuid)).getOne();
    }

    @Override
    public Client getClientById(int id) {
        return new ModelFilter<ClientImpl>().model(ClientImpl.class).cache((client) -> client.getId() == id).getOne();
    }

    @Override
    public void setShutdownHook(Runnable shutdownHook) {
        this.shutdownHook = shutdownHook;
    }

    @Override
    public Runnable getShutdownHook() {
        return shutdownHook;
    }

    @Override
    public List<Class<? extends IModule>> getCachedGodModules() {
        return cachedGodModules;
    }

    @Override
    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public TabManager getTabManager() {
        return this.tabManager;
    }

    @Override
    public void setTabManager(TabManager tabManager) {
        this.tabManager = tabManager;
    }

    @Override
    public long serverTick() {
        return this.serverTick;
    }

    @Override
    public void tick() {
        this.serverTick++;
    }

    @Override
    public void shutdown() {
        this.shutdown("Shutdown hook manually called. Please check what's happening.");
    }

    @Override
    public void addJoinCallback(Module module, TriConsumer<PlayerJoinEvent, Player, Client> callback) {
        List<TriConsumer<PlayerJoinEvent, Player, Client>> moduleCallbacks = this.joinCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.joinCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addQuitCallbacks(Module module, TriConsumer<PlayerQuitEvent, Player, Client> callback) {
        List<TriConsumer<PlayerQuitEvent, Player, Client>> moduleCallbacks = this.quitCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.quitCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void removeJoinCallbacks(Module module) {
        this.joinCallbacks.remove(module);
    }

    @Override
    public void removeQuitCallbacks(Module module) {
        this.quitCallbacks.remove(module);
    }

    @Override
    public Map<Module, List<TriConsumer<PlayerJoinEvent, Player, Client>>> getJoinCallbacks() {
        return joinCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<PlayerQuitEvent, Player, Client>>> getQuitCallbacks() {
        return quitCallbacks;
    }

    @Override
    public PipeText text() {
        return this.pipeText;
    }

    @Override
    public void shutdown(String reason) {
        if (shutdownHook != null) {
            Logger.err(reason);
            shutdownHook.run();
        }
    }
}
