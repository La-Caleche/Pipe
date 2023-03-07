package fr.lacaleche.pipe.bukkit.modules.command.listeners;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.CompleterImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.colors.AsciiColors;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.apache.commons.lang3.function.TriFunction;
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
        CoreCommandImpl command = parseCommand(event, event.getPlayer(), message);
        if (command == null) {
            if (event.isCancelled()) {
                event.getPlayer().sendMessage(client.getLocale().t("pipe.helper.command_not_found").arg("label", message.split(" ")[0]).ct());
            }
            return;
        }
        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCESS) {
            Pipe.get().getCommandManager().parseCommandResult(command, command.getCommandSender(), result);
        }
    }

    /**
     * No PipeDebug.eventCalled() for this event because of spam.
     * */
    @EventHandler
    public void onConsoleExecute(ServerCommandEvent event) {
        Locale locale = Pipe.get().getDefaultLocale();
        String message = event.getCommand();
        CoreCommandImpl command = parseCommand(event, event.getSender(), message);
        if (command == null) {
            if (event.isCancelled()) {
                event.getSender().sendMessage(locale.t("pipe.helper.command_not_found").arg("label", message.split(" ")[0]).ct());
            }
            return;
        }
        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCESS) {
            Pipe.get().getCommandManager().parseCommandResult(command, command.getCommandSender(), result);
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
            if (client != null && client.getRank().getPermissionLevel() < 20) PipeDebug.setCancelled(event, true);
        }
        return manager.handleCommand(sender, label, message, arguments);
    }

}
