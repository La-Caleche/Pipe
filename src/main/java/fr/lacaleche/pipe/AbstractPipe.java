package fr.lacaleche.pipe;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.adventure.PipeTextImpl;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.common.commands.interfaces.PipeCommandManager;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tasks.interfaces.TaskManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.*;

public abstract class AbstractPipe implements Pipe {

    private static Pipe instance;

    private PipeCommandManager<?> commandManager;
    private Runnable shutdownHook;
    private TaskManager taskManager;
    private final PipeText pipeText;
    private Locale defaultLocale;

    private final Map<Object, Client> recipientsCache = new HashMap<>();

    private Class<? extends ClientImpl> clientClass;

    private long serverTick;

    public AbstractPipe() {
        instance = this;
        this.pipeText = new PipeTextImpl();

        this.serverTick = 0;
        this.clientClass = ClientImpl.class;
    }

    public static Pipe get() {
        if (instance == null) throw new IllegalStateException("Pipe instance is null");
        return instance;
    }

    @Override
    public PipeCommandManager<?> getCommandManager() {
        return commandManager;
    }

    @Override
    public <C> void setCommandManager(PipeCommandManager<C> commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public Locale getDefaultLocale() {
        if (this.defaultLocale == null)
            this.defaultLocale = new ModelFilter<LocaleImpl>().model(LocaleImpl.class).sql(sql -> sql.where(new Where("is_default", true))).cache(LocaleImpl::isDefault).getOne();
        return this.defaultLocale;
    }

    @Override
    public <C> @NotNull Locale getLocale(C recipient) {
        final Client client = this.getClient(recipient);
        return client == null ? this.getDefaultLocale() : client.getLocale();
    }

    /**
     * Get the client from a generic recipient
     * <p>
     * Exception ignored because the sender is not a player
     * and we don't want to crash the server or show any error message
     *
     * @param recipient the recipient
     * @param <C>       the type of the recipient
     * @return the client
     */
    @Override
    public <C> @Nullable Client getClient(C recipient) {
        Client client = this.recipientsCache.getOrDefault(recipient, null);
        if (client != null) return client;

        Reflect reflect = Reflect.on(recipient);
        try {
            UUID uuid = reflect.call("getUniqueId").get();
            if (uuid != null) {
                client = this.getClient(uuid);
                if (client != null) this.recipientsCache.put(recipient, client);
            }
        } catch (ReflectException ignored) {}
        return client;
    }

    @Override
    public Client getClient(UUID uuid) {
        return new ModelFilter<ClientImpl>().model((Class<ClientImpl>) Pipe.clientClass()).cache((client) -> client.getUUID().equals(uuid)).disableSql().getOne();
    }

    @Override
    public Client getClientById(int id) {
        return new ModelFilter<ClientImpl>().model((Class<ClientImpl>) Pipe.clientClass()).cache((client) -> client.getId() == id).disableSql().getOne();
    }

    @Override
    public Client instanciateClient(UUID uuid, String username) {
        try {
            return this.clientClass.getConstructor(UUID.class, String.class).newInstance(uuid, username);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Class<? extends ClientImpl> getClientClass() {
        return clientClass;
    }

    @Override
    public void setClientClass(Class<? extends ClientImpl> clientClass) {
        this.clientClass = clientClass;
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
