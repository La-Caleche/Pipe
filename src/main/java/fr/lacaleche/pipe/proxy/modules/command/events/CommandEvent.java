package fr.lacaleche.pipe.proxy.modules.command.events;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.core.events.interfaces.Cancellable;

public class CommandEvent extends CoreEvent implements Cancellable {

    private final String command;
    private final CommandSource source;
    private boolean cancelled;

    public CommandEvent(final CommandSource source, final String command) {
        this.command = command;
        this.source = source;
    }

    public String getCommand() {
        return command;
    }

    public CommandSource getSource() {
        return source;
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
