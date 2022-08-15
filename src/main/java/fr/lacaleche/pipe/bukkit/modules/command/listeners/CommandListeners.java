package fr.lacaleche.pipe.bukkit.modules.command.listeners;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.CompleterImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.colors.AsciiColors;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandListeners implements Listener {

    @EventHandler
    public void onCommandExecute(PlayerCommandPreprocessEvent event) {
        PipeDebug.eventCalled(event);
        Logger.customDebugWCheck(AsciiColors.YELLOW + "Command: " + event.getMessage());
        String message = event.getMessage().substring(1);
        CoreCommandImpl command = parseCommand(event, event.getPlayer(), message);
        if (command == null) {
            if (event.isCancelled())
                event.getPlayer().sendMessage("command_not_found (missing locale module)");
            return;
        }
        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCESS) event.getPlayer().sendMessage("(missing HelpUtils %s)".formatted(result.toString()));
    }

    /**
     * No PipeDebug.eventCalled() for this event because of spam.
     * */
    @EventHandler
    public void onConsoleExecute(ServerCommandEvent event) {
        String message = event.getCommand();
        CoreCommandImpl command = parseCommand(event, event.getSender(), message);
        if (command == null) {
            if (event.isCancelled())
                event.getSender().sendMessage("command_not_found (missing locale module)");
            return;
        }
        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCESS) event.getSender().sendMessage("(missing HelpUtils %s)".formatted(result.toString()));
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        PipeDebug.eventCalled(event);
        for (Class<MinecraftCommand> value : Pipe.get().getCommandManager().getCommands().values()) {
            MinecraftCommand command = CommandsUtils.validateCommand(value);
            event.getCommands().add(command.label());
            event.getCommands().addAll(Arrays.asList(command.aliases()));
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        PipeDebug.eventCalled(event);
        String message = event.getBuffer().substring(1);
        String[] splitted = message.split(" ");
        CoreCommandImpl command = parseCommand(event, event.getSender(), message);

        if (command == null) {
            event.getCompletions().clear();
            return;
        }

        int userArgumentsLength = command.getUserArguments().length;
        String buffer = command.getUserInput().endsWith(" ") ? command.getUserInput().trim() + " " : command.getUserInput();
        if (userArgumentsLength == 0 && !buffer.endsWith(" ")) return;

        Completer completer = new CompleterImpl(command, event.getSender());
        completer.setIndex(command.getUserArguments().length);
        completer.setNext(userArgumentsLength == 0 || buffer.endsWith(command.getUserArguments()[userArgumentsLength - 1] + " "));

        Pipe.get().getCommandManager().parseCompleter(Pipe.get().getPlugin(), completer);
        event.setCompletions(completer.getCompleter().stream().sorted().collect(Collectors.toList()));
    }

    private CoreCommandImpl parseCommand(Cancellable event, Object sender, String message) {
        CommandManager manager = Pipe.get().getCommandManager();
        String[] fullArguments = message.split(" ");
        String label = fullArguments[0];
        String[] arguments = Arrays.copyOfRange(fullArguments, 1, fullArguments.length);
        if (!(event instanceof TabCompleteEvent)) {
            Client client = manager.getClient(sender);
            if (!manager.isNativeCommand(label)) PipeDebug.setCancelled(event, true);
            else if (client != null && client.getRank().getPermissionLevel() < 20) PipeDebug.setCancelled(event, true);
        }
        return manager.handleCommand(sender, label, message, arguments);
    }

}
