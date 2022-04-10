package fr.lacaleche.pipe.proxy.commands;

import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.proxy.modules.command.events.CommandEvent;
import fr.lacaleche.pipe.proxy.modules.command.events.TabCompleteEvent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class SimpleProxyPipeCommand extends Command implements TabExecutor {

    public SimpleProxyPipeCommand(MinecraftCommand command) {
        super(command.label());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CommandEvent event = new CommandEvent(sender, "%s %s".formatted(getName(), String.join(" ", args)));
        event.call();
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        TabCompleteEvent event = new TabCompleteEvent(sender, "%s %s".formatted(getName(), String.join(" ", args)));
        event.call();

        return event.getCompletions();
    }

}
