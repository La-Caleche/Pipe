package fr.lacaleche.pipe;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PipeImpl implements Pipe {

    public static Pipe instance;
    private Object plugin;
    private CommandManager commandManager;
    private Runnable shutdownHook;
    private List<Class<? extends IModule>> cachedGodModules;
    private TaskManager taskManager;
    private TabManager tabManager;
    private final List<Object> plugins;

    private long serverTick;

    public PipeImpl() {
        this.plugins = new ArrayList<>();
        this.cachedGodModules = new ArrayList<>();

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
        return new ModelFilter<LocaleImpl>().find(LocaleImpl.class, LocaleImpl::isDefault);
    }

    @Override
    public Client getClient(UUID uuid) {
        return new ModelFilter<ClientImpl>().find(ClientImpl.class, (client) -> client.getUUID().equals(uuid));
    }

    @Override
    public Client getClientById(int id) {
        return new ModelFilter<ClientImpl>().find(ClientImpl.class, (client) -> client.getId() == id);
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
    public void shutdown(String reason) {
        if (shutdownHook != null) {
            Logger.err(reason);
            shutdownHook.run();
        }
    }
}
