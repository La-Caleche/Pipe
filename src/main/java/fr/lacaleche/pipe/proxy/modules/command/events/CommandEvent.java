package fr.lacaleche.pipe.proxy.modules.command.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.core.events.interfaces.Cancellable;
import net.md_5.bungee.api.CommandSender;

public class CommandEvent extends CoreEvent implements Cancellable {

    private final String command;
    private final CommandSender sender;
    private boolean cancelled;

    public CommandEvent(final CommandSender sender, final String command) {
        this.command = command;
        this.sender = sender;
    }

    public String getCommand() {
        return command;
    }

    public CommandSender getSender() {
        return sender;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

}
