package fr.lacaleche.pipe.bukkit.modules.command.listeners;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.core.utils.logger.Logger;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CommandListeners implements Listener {

    @EventHandler
    public void onCommandExecute(PlayerCommandPreprocessEvent event) {
        PipeDebug.eventCalled(event);
        Client client = Pipe.getBukkit().getClient(event.getPlayer().getUniqueId());
        String message = event.getMessage().substring(1);
        List<String> splitted = Splitter.on(' ').splitToList(message);
        IPair<CoreCommandImpl, CommandResult> pair = parseCommand(event, event.getPlayer(), message);
        CoreCommandImpl command = pair.getLeft();

        if (command == null) {
            CommandResult result = pair.getRight();
            if (result == CommandResult.COMMAND_NOT_FOUND)
                event.getPlayer().sendMessage(client.getLocale()
                        .t("pipe.helper.command_not_found").arg("label", splitted.get(0)).ct());
            else if (result == CommandResult.MISSING_PERMISSION)
                event.getPlayer().sendMessage(client.getLocale()
                        .t("pipe.helper.permission_denied").arg("label", splitted.get(0)).ct());
            return;
        }

        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCCESS) {
            Pipe.getBukkit().getCommandManager().parseCommandResult(command, command.getCommandSender(), result);
        }
    }

    /**
     * No PipeDebug.eventCalled() for this event because of spam.
     * */
    @EventHandler
    public void onConsoleExecute(ServerCommandEvent event) {
        Locale locale = Pipe.getBukkit().getDefaultLocale();
        String message = event.getCommand();
        List<String> splitted = Splitter.on(' ').splitToList(message);
        IPair<CoreCommandImpl, CommandResult> pair = parseCommand(event, event.getSender(), message);
        CoreCommandImpl command = pair.getLeft();

        if (command == null) {
            CommandResult result = pair.getRight();
            if (result == CommandResult.COMMAND_NOT_FOUND)
                event.getSender().sendMessage(locale
                        .t("pipe.helper.command_not_found").arg("label", splitted.get(0)).ct());
            else if (result == CommandResult.MISSING_PERMISSION)
                event.getSender().sendMessage(locale
                        .t("pipe.helper.permission_denied").arg("label", splitted.get(0)).ct());
            return;
        }

        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCCESS) {
            Pipe.getBukkit().getCommandManager().parseCommandResult(command, command.getCommandSender(), result);
        }
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        PipeDebug.eventCalled(event);
        for (Class<MinecraftCommand> value : Pipe.getBukkit().getCommandManager().getCommands().values()) {
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
        IPair<CoreCommandImpl, CommandResult> command = parseTabCommand(event.getSender(), message);
        CoreCommandImpl commandImpl = command.getLeft();

        if (command.getRight() == CommandResult.NOT_LC_COMMAND) return;
        else if (command.getRight() == CommandResult.COMMAND_NOT_FOUND
                || command.getRight() == CommandResult.MISSING_PERMISSION) {
            event.setCancelled(true);
            return;
        }

        event.getCompletions().clear();

        int userArgumentsLength = commandImpl.getUserArguments().size();
        buffer = commandImpl.getUserInput().endsWith(" ")
                ? commandImpl.getUserInput().trim() + " "
                : commandImpl.getUserInput();
        if (userArgumentsLength == 0 && !buffer.endsWith(" ")) return;

        Completer completer = new CompleterImpl(commandImpl, event.getSender());
        completer.setIndex(commandImpl.getUserArguments().size());
        completer.setNext(userArgumentsLength == 0
                || buffer.endsWith(commandImpl.getUserArguments().get(userArgumentsLength - 1) + " "));

        Pipe.getBukkit().getCommandManager().parseCompleter(Pipe.getBukkit().getPlugin(), completer);
        event.setCompletions(completer.getSortedCompleter());
    }

    private IPair<CoreCommandImpl, CommandResult> parseCommand(Cancellable event, Object sender, String message) {
        CommandManager manager = Pipe.getBukkit().getCommandManager();
        List<String> fullArguments = Splitter.on(' ').splitToList(message);
        String label = fullArguments.get(0);
        List<String> arguments = fullArguments.stream().skip(1).toList();
        Client client = manager.getClient(sender);

        CommandResult result = CommandResult.COMMAND_SUCCESS;

        if (!manager.commandExist(label)) result = CommandResult.COMMAND_NOT_FOUND;
        else if (client != null && client.getRank().getPermissionLevel() < 20)
            result = CommandResult.MISSING_PERMISSION;

        if (result != CommandResult.COMMAND_SUCCESS) {
            event.setCancelled(true);
            return new Pair<>(null, result);
        }

        List<String> tmpLabel = Splitter.on(':').splitToList(label);
        if (tmpLabel.size() > 1 && manager.commandExist(tmpLabel.get(1)))
            label = tmpLabel.get(1);

        CoreCommandImpl command = manager.handleCommand(sender, label, message, arguments);
        if (command == null) result = CommandResult.NOT_LC_COMMAND;

        return new Pair<>(command, result);
    }

    private IPair<CoreCommandImpl, CommandResult> parseTabCommand(Object sender, String message) {
        CommandManager manager = Pipe.getBukkit().getCommandManager();
        List<String> fullArguments = Splitter.on(' ').splitToList(message);
        String label = fullArguments.get(0);
        List<String> arguments = fullArguments.stream().skip(1).toList();
        Client client = manager.getClient(sender);

        CommandResult result = CommandResult.COMMAND_SUCCESS;

        if (!manager.commandExist(label)) result = CommandResult.COMMAND_NOT_FOUND;
        else if (client != null && client.getRank().getPermissionLevel() < 20)
            result = CommandResult.MISSING_PERMISSION;

        List<String> tmpLabel = Splitter.on(':').splitToList(label);
        if (tmpLabel.size() > 1 && manager.commandExist(tmpLabel.get(1)))
            label = tmpLabel.get(1);

        CoreCommandImpl command = manager.handleCommand(sender, label, message, arguments);
        if (command == null) result = CommandResult.NOT_LC_COMMAND;

        return new Pair<>(command, result);
    }

}
