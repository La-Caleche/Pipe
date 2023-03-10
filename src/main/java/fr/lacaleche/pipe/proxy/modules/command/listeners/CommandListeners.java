package fr.lacaleche.pipe.proxy.modules.command.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
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
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.proxy.modules.command.events.CommandEvent;
import fr.lacaleche.pipe.proxy.modules.command.events.TabCompleteEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandListeners implements CoreListener {

    @CoreEventHandler
    public void onTabComplete(TabCompleteEvent event) {
        PipeDebug.eventCalled(event);
        String message = event.getBuffer();
        String[] splitted = message.split(" ");
        CoreCommandImpl command = parseCommand(event, event.getSource(), message);

        if (command == null) {
            event.getCompletions().clear();
            return;
        }

        int userArgumentsLength = command.getUserArguments().length;
        String buffer = command.getUserInput().endsWith(" ") ? command.getUserInput().trim() + " " : command.getUserInput();
        if (userArgumentsLength == 0 && !buffer.endsWith(" ")) return;

        Completer completer = new CompleterImpl(command, event.getSource());
        completer.setIndex(command.getUserArguments().length);
        completer.setNext(userArgumentsLength == 0 || buffer.endsWith(command.getUserArguments()[userArgumentsLength - 1] + " "));

        Pipe.get().getCommandManager().parseCompleter(event.getSource(), completer);
        event.setCompletions(completer.getCompleter().stream().sorted().collect(Collectors.toList()));
    }

    @CoreEventHandler
    public void onCommandEvent(CommandEvent event) {
        Locale locale = Pipe.get().getDefaultLocale();
        if (event.getSource() instanceof Player player) locale = Pipe.get().getClient(player.getUniqueId()).getLocale();
        String message = event.getCommand();
        CoreCommandImpl command = parseCommand(event, event.getSource(), message);
        if (command == null) {
            if (event.isCancelled()) {
                event.getSource ().sendMessage(locale.t("pipe.helper.command_not_found").arg("label", message.split(" ")[0]).ct());
            }
            return;
        }
        CommandResult result = command.execute();
        if (result != CommandResult.COMMAND_SUCESS) {
            Pipe.get().getCommandManager().parseCommandResult(command, command.getCommandSender(), result);
        }
    }

    private CoreCommandImpl parseCommand(Cancellable event, Object sender, String message) {
        CommandManager manager = Pipe.get().getCommandManager();
        String[] fullArguments = message.split(" ");
        String label = fullArguments[0];
        String[] arguments = Arrays.copyOfRange(fullArguments, 1, fullArguments.length);
        if (!(event instanceof TabCompleteEvent)) {
            Client client = manager.getClient(sender);
            if (!manager.commandExist(label)) PipeDebug.setCancelled(event, true);
            else if (client != null && client.getRank().getPermissionLevel() < 20) PipeDebug.setCancelled(event, true);
        }
        return manager.handleCommand(sender, label, message, arguments);
    }

}
