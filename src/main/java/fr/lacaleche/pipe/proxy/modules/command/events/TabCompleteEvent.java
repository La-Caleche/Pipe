package fr.lacaleche.pipe.proxy.modules.command.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.core.events.interfaces.Cancellable;
import fr.lacaleche.core.utils.redis.packet.Packet;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteEvent extends CoreEvent implements Cancellable {

    private final CommandSender sender;
    private final String buffer;
    private List<String> completions;
    private boolean cancelled;

    public TabCompleteEvent(CommandSender sender, String buffer) {
        this.sender = sender;
        this.buffer = buffer;
        this.completions = new ArrayList<>();
    }

    public TabCompleteEvent(CommandSender sender, String buffer, List<String> completions) {
        this.sender = sender;
        this.buffer = buffer;
        this.completions = completions;
    }

    public CommandSender getSender() {
        return sender;
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
