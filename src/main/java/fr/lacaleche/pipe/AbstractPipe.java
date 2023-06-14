package fr.lacaleche.pipe;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.events.GlobalListenerManager;
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

public abstract class AbstractPipe implements Pipe {

    private static Pipe instance;

    private CommandManager commandManager;
    private Runnable shutdownHook;
    private TaskManager taskManager;
    private final PipeText pipeText;
    private Locale defaultLocale;

    private long serverTick;

    public AbstractPipe() {
        instance = this;
        this.pipeText = new PipeTextImpl();

        this.serverTick = 0;
    }

    public static Pipe get() {
        if (instance == null) throw new IllegalStateException("Pipe instance is null");
        return instance;
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
    public Locale getDefaultLocale() {
        if (this.defaultLocale == null)
            this.defaultLocale = new ModelFilter<LocaleImpl>().model(LocaleImpl.class).cache(LocaleImpl::isDefault).getOne();
        return this.defaultLocale;
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
    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
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
    public PipeText text() {
        return this.pipeText;
    }

    @Override
    public GlobalListenerManager globalListenerManager() {
        return Core.get().getListenerManager();
    }

    @Override
    public void shutdown(String reason) {
        if (shutdownHook != null) {
            Logger.err(reason);
            shutdownHook.run();
        }
    }

}
