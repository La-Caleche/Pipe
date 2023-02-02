package fr.lacaleche.pipe;

import com.comphenix.protocol.ProtocolManager;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.events.interfaces.IListenerManager;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.platform.AudienceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PipeImpl implements Pipe {

    public static Pipe instance;
    private Object plugin;
    private CommandManager commandManager;
    private AudienceProvider adventure;
    private Runnable shutdownHook;
    private ProtocolManager protocolManager;

    private List<Class<? extends IModule>> cachedGodModules;
    private final List<Object> plugins;

    public PipeImpl() {
        this.plugins = new ArrayList<>();
        this.cachedGodModules = new ArrayList<>();
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
        return CalecheCore.get().getListenerManager();
    }

    @Override
    public Locale getDefaultLocale() {
        return new ModelFilter<LocaleImpl>().find(LocaleImpl.class, LocaleImpl::isDefault);
    }

    @Override
    public void setAdventure(AudienceProvider adventure) {
        this.adventure = adventure;
    }

    @Override
    public <T extends AudienceProvider> T adventure() {
        return (T) this.adventure;
    }

    @Override
    public Client getClient(UUID uuid) {
        return new ModelFilter<ClientImpl>().find(ClientImpl.class, (client) -> client.getUUID().equals(uuid));
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
    public void setProtocolManager(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    @Override
    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public List<Class<? extends IModule>> getCachedGodModules() {
        return cachedGodModules;
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
