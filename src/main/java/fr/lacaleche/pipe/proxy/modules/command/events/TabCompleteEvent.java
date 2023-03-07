package fr.lacaleche.pipe.proxy.modules.command.events;

import com.velocitypowered.api.command.CommandSource;
import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.core.events.interfaces.Cancellable;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteEvent extends CoreEvent implements Cancellable {

    private final CommandSource source;
    private final String buffer;
    private List<String> completions;
    private boolean cancelled;

    public TabCompleteEvent(final CommandSource source, String buffer) {
        this(source, buffer, new ArrayList<>());
    }

    public TabCompleteEvent(final CommandSource source, String buffer, List<String> completions) {
        this.source = source;
        this.buffer = buffer;
        this.completions = completions;
    }

    public CommandSource getSource() {
        return source;
    }

    public List<String> getCompletions() {
        return completions;
    }

    public String getBuffer() {
        return buffer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCompletions(List<String> completions) {
        this.completions = new ArrayList<>(completions);
    }

}
