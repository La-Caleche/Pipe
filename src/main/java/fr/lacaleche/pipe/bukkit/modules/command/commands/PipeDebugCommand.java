package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "pipedebug", description = "pipe.command.pipedebug.description")
public class PipeDebugCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        sender.sendMessage(locale.ct("pipe.command.pipedebug.dev_status.enabled", "pipe.command.pipedebug.dev_status.disabled", CalecheCore.get().inDev()).ct());
        sender.sendMessage(locale.ct("pipe.command.pipedebug.debug_status.enabled", "pipe.command.pipedebug.debug_status.disabled", CalecheCore.get().debugEnabled()).ct());

        return true;
    }

    @CommandChild(label = "switch", description = "pipe.command.pipedebug.switch.description")
    public static class Switch {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            CalecheCore.get().setDebugEnabled(!CalecheCore.get().debugEnabled());
            sender.sendMessage(locale.ct("pipe.command.pipedebug.debug_status.enabled", "pipe.command.pipedebug.debug_status.disabled", CalecheCore.get().debugEnabled()).ct());
            return true;
        }

    }

}
