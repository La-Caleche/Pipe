package fr.lacaleche.pipe.proxy.modules.command.listeners;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.argument.CompleterImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.core.events.annotations.CoreEventHandler;
import fr.lacaleche.core.events.interfaces.Cancellable;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.pipe.proxy.modules.command.events.CommandEvent;
import fr.lacaleche.pipe.proxy.modules.command.events.TabCompleteEvent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandListener implements CoreListener, Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
    }

    @CoreEventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String message = event.getBuffer();
        String[] splitted = message.split(" ");
        CoreCommandImpl command = parseCommand(event, event.getSender(), message);

        if (command == null) {
            event.getCompletions().clear();
            return;
        }

        int userArgumentsLength = command.getUserArguments().length;
        String buffer = command.getUserInput().endsWith(" ") ? command.getUserInput().trim() + " " : command.getUserInput();
        if (userArgumentsLength == 0 && !buffer.endsWith(" ")) return;

        Completer completer = new CompleterImpl(command);
        completer.setIndex(command.getUserArguments().length);
        completer.setNext(userArgumentsLength == 0 || buffer.endsWith(command.getUserArguments()[userArgumentsLength - 1] + " "));

        Pipe.get().getCommandManager().parseCompleter((Plugin) Pipe.get().getPlugin(), completer);
        event.setCompletions(completer.getCompleter().stream().sorted().collect(Collectors.toList()));
    }

    @CoreEventHandler
    public void onCommandEvent(CommandEvent event) {
        String message = event.getCommand();
        CoreCommandImpl command = parseCommand(event, event.getSender(), message);
        if (command == null)
            return;
        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCESS) event.getSender().sendMessage("(missing HelpUtils %s)".formatted(result.toString()));
    }

    private CoreCommandImpl parseCommand(Cancellable event, Object sender, String message) {
        CommandManager manager = Pipe.get().getCommandManager();
        String[] fullArguments = message.split(" ");
        String label = fullArguments[0];
        String[] arguments = Arrays.copyOfRange(fullArguments, 1, fullArguments.length);
        if (!(event instanceof TabCompleteEvent)) {
            Client client = manager.getClient(sender);
            if (!manager.isNativeCommand(label)) event.setCancelled(true);
            else if (client != null && client.getRank().getPermissionLevel() < 20) event.setCancelled(true);
        }
        return manager.handleCommand(sender, label, message, arguments);
    }

}
