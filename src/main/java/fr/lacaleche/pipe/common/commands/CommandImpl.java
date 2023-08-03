package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.jetbrains.annotations.Nullable;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.UUID;

public class CommandImpl<T> implements Command<T> {

    private T sender;
    private Arguments args;
    private Locale locale;

    public CommandImpl(T sender, Arguments args) {
        this.sender = sender;
        this.args = args;
        this.locale = Pipe.get().getDefaultLocale();

        Reflect reflect = Reflect.on(sender);
        try {
            UUID uuid = reflect.call("getUniqueId").get();
            if (uuid != null) {
                this.locale = Pipe.get().getClient(uuid).getLocale();
            }
        } catch (ReflectException ignored) {
            // Exception ignored because the sender is not a player
            // and we don't want to crash the server or show any error message
        }
    }

    @Override
    public T sender() {
        return sender;
    }

    @Override
    public Arguments args() {
        return args;
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Nullable
    @Override
    public Client clientSender() {
        Reflect reflect = Reflect.on(sender);
        try {
            UUID uuid = reflect.call("getUniqueId").get();
            if (uuid != null) {
                return Pipe.get().getClient(uuid);
            }
        } catch (ReflectException ignored) {
            // Exception ignored because the sender is not a player
            // and we don't want to crash the server or show any error message
        }
        return null;
    }
}
