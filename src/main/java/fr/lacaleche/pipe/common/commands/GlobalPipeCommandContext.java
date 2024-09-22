package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.enums.CommandExecutor;
import fr.lacaleche.pipe.common.commands.interfaces.PipeCommandContext;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;

import java.lang.reflect.Method;
import java.util.Optional;

public class GlobalPipeCommandContext<C> implements PipeCommandContext<C> {

    private final C sender;
    private Locale locale;
    private Client client;

    public GlobalPipeCommandContext(final C sender) {
        this.sender = sender;
        this.client = Pipe.get().getClient(sender);
        this.locale = Pipe.get().getLocale(sender);
    }

    @Override
    public Client client() {
        return client;
    }

    @Override
    public <T> Optional<Client> searchClient(T target) {
        return Optional.ofNullable(Pipe.get().getClient(target));
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public C sender() {
        return sender;
    }

    @Override
    public Client clientOrReject(Client client) {
        if (this.client() == null && client == null) {
            this.sendMessage(locale().t("global.only_for_players").ct());
            return null;
        }
        return client == null ? this.client() : client;
    }

    @Override
    public CommandExecutor executor() {
        return CommandExecutor.COMMON;
    }

    @Override
    public void sendMessage(String message) {
        final Method method = getSendMessageMethod(String.class);
        if (method == null) {
            Logger.customDebug("Method sendMessage(String) not found for %s", sender.getClass().getName());
            return ;
        }

        this.safeInvoke(method, message);
    }

    @Override
    public void sendMessage(Component component) {
        final Method method = getSendMessageMethod(Component.class);
        if (method == null) {
            Logger.customDebug("Method sendMessage(Component) not found for %s", sender.getClass().getName());
            return;
        }

        this.safeInvoke(method, component);
    }

    private Method getSendMessageMethod(Class<?>... params) {
        try {
            return this.sender.getClass().getMethod("sendMessage", params);
        } catch (NoSuchMethodException ignored) {}
        return null;
    }

    private void safeInvoke(Method method, Object... args) {
        try {
            method.invoke(sender, args);
        } catch (Exception e) {
            Logger.customDebug("Error invoking method %s for %s", method.getName(), sender.getClass().getName());
        }
    }

}
