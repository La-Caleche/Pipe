package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.interfaces.CommandContext;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.UUID;

public abstract class GlobalCommandContext<C> implements CommandContext<C> {

    private final C sender;
    private Locale locale;
    private Client client;

    public GlobalCommandContext(final C sender) {
        this.sender = sender;
        this.locale = Pipe.get().getDefaultLocale();

        Reflect reflect = Reflect.on(sender);
        try {
            UUID uuid = reflect.call("getUniqueId").get();
            if (uuid != null) {
                this.client = Pipe.get().getClient(uuid);
                this.locale = this.client.getLocale();
            }
        } catch (ReflectException ignored) {
            // Exception ignored because the sender is not a player
            // and we don't want to crash the server or show any error message
        }
    }

    @Override
    public Client client() {
        return client;
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public C sender() {
        return sender;
    }
}
