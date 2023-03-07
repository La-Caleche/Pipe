package fr.lacaleche.pipe.proxy.commands;

import com.velocitypowered.api.command.SimpleCommand;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.proxy.modules.command.events.CommandEvent;
import fr.lacaleche.pipe.proxy.modules.command.events.TabCompleteEvent;

import java.util.List;

public class SimpleProxyPipeCommand implements SimpleCommand {

    private String label;

    public SimpleProxyPipeCommand(MinecraftCommand command) {
        this.label = command.label();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandEvent event = new CommandEvent(invocation.source(), "%s %s".formatted(invocation.alias(), String.join(" ", invocation.arguments())));
        event.call();
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        TabCompleteEvent event = new TabCompleteEvent(invocation.source(), "%s %s".formatted(invocation.alias(), String.join(" ", invocation.arguments())));
        event.call();

        return event.getCompletions();
    }

}
