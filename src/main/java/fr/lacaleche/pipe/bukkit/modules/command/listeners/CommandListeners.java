package fr.lacaleche.pipe.bukkit.modules.command.listeners;

import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.CompleterImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
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
        Client client = Pipe.get().getClient(event.getPlayer().getUniqueId());
        String message = event.getMessage().substring(1);
        IPair<CoreCommandImpl, CommandResult> command = parseCommand(event, event.getPlayer(), message);
        CoreCommandImpl commandImpl = command.getLeft();

        if (commandImpl == null) {
            CommandResult result = command.getRight();
            if (result == CommandResult.COMMAND_NOT_FOUND)
                event.getPlayer().sendMessage(client.getLocale().t("pipe.helper.command_not_found").arg("label", message.split(" ")[0]).ct());
            else if (result == CommandResult.MISSING_PERMISSION)
                event.getPlayer().sendMessage(client.getLocale().t("pipe.helper.permission_denied").arg("label", message.split(" ")[0]).ct());
            return;
        }

        CommandResult result = commandImpl.execute();
        if (result != CommandResult.COMMAND_SUCCESS) {
            Pipe.get().getCommandManager().parseCommandResult(commandImpl, commandImpl.getCommandSender(), result);
        }
    }

    /**
     * No PipeDebug.eventCalled() for this event because of spam.
     * */
    @EventHandler
    public void onConsoleExecute(ServerCommandEvent event) {
        Locale locale = Pipe.get().getDefaultLocale();
        String message = event.getCommand();
        IPair<CoreCommandImpl, CommandResult> command = parseCommand(event, event.getSender(), message);
        CoreCommandImpl commandImpl = command.getLeft();

        if (commandImpl == null) {
            CommandResult result = command.getRight();
            if (result == CommandResult.COMMAND_NOT_FOUND)
                event.getSender().sendMessage(locale.t("pipe.helper.command_not_found").arg("label", message.split(" ")[0]).ct());
            else if (result == CommandResult.MISSING_PERMISSION)
                event.getSender().sendMessage(locale.t("pipe.helper.permission_denied").arg("label", message.split(" ")[0]).ct());
            return;
        }

        CommandResult result = commandImpl.execute();
        if (result != CommandResult.COMMAND_SUCCESS) {
            Pipe.get().getCommandManager().parseCommandResult(commandImpl, commandImpl.getCommandSender(), result);
        }
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
        String buffer = event.getBuffer();
        String message = buffer.startsWith("/") ? buffer.substring(1) : buffer;
        String[] splitted = message.split(" ");
        IPair<CoreCommandImpl, CommandResult> command = parseTabCommand(event, event.getSender(), message);
        CoreCommandImpl commandImpl = command.getLeft();

        if (command.getRight() == CommandResult.NOT_LC_COMMAND) return;
        else if (command.getRight() == CommandResult.COMMAND_NOT_FOUND || command.getRight() == CommandResult.MISSING_PERMISSION) {
            event.setCancelled(true);
            return;
        }

        event.getCompletions().clear();

        int userArgumentsLength = commandImpl.getUserArguments().length;
        buffer = commandImpl.getUserInput().endsWith(" ") ? commandImpl.getUserInput().trim() + " " : commandImpl.getUserInput();
        if (userArgumentsLength == 0 && !buffer.endsWith(" ")) return;

        Completer completer = new CompleterImpl(commandImpl, event.getSender());
        completer.setIndex(commandImpl.getUserArguments().length);
        completer.setNext(userArgumentsLength == 0 || buffer.endsWith(commandImpl.getUserArguments()[userArgumentsLength - 1] + " "));

        Pipe.get().getCommandManager().parseCompleter(Pipe.get().getPlugin(), completer);
        event.setCompletions(completer.getCompleter().stream().sorted().collect(Collectors.toList()));
    }

    private IPair<CoreCommandImpl, CommandResult> parseCommand(Cancellable event, Object sender, String message) {
        CommandManager manager = Pipe.get().getCommandManager();
        String[] fullArguments = message.split(" ");
        String label = fullArguments[0];
        String[] arguments = Arrays.copyOfRange(fullArguments, 1, fullArguments.length);
        Client client = manager.getClient(sender);

        CommandResult result = CommandResult.COMMAND_SUCCESS;

        if (!manager.commandExist(label)) result = CommandResult.COMMAND_NOT_FOUND;
        else if (client != null && client.getRank().getPermissionLevel() < 20) result = CommandResult.MISSING_PERMISSION;

        if (result != CommandResult.COMMAND_SUCCESS) {
            event.setCancelled(true);
            return new Pair<>(null, result);
        }

        CoreCommandImpl command = manager.handleCommand(sender, label, message, arguments);
        if (command == null) result = CommandResult.NOT_LC_COMMAND;
        return new Pair<>(command, result);
    }

    private IPair<CoreCommandImpl, CommandResult> parseTabCommand(Cancellable event, Object sender, String message) {
        CommandManager manager = Pipe.get().getCommandManager();
        String[] fullArguments = message.split(" ");
        String label = fullArguments[0];
        String[] arguments = Arrays.copyOfRange(fullArguments, 1, fullArguments.length);
        Client client = manager.getClient(sender);

        CommandResult result = CommandResult.COMMAND_SUCCESS;

        if (!manager.commandExist(label)) result = CommandResult.COMMAND_NOT_FOUND;
        else if (client != null && client.getRank().getPermissionLevel() < 20) result = CommandResult.MISSING_PERMISSION;

        CoreCommandImpl command = manager.handleCommand(sender, label, message, arguments);
        if (command == null) result = CommandResult.NOT_LC_COMMAND;
        return new Pair<>(command, result);
    }

}
