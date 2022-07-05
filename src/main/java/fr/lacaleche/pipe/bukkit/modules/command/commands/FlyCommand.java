package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "fly", description = "pipe.command.fly.description", arguments = {"player"})
public class FlyCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(sender, arguments, "player");
        if (result.hasError()) {
            sender.sendMessage(result.getError().ct());
            return true;
        }

        Player target = result.getPlayer();

        target.setAllowFlight(!target.getAllowFlight());
        sender.sendMessage(locale.ct("pipe.command.fly.success.enabled", "pipe.command.fly.success.disabled", target.getAllowFlight()).arg("player", target.getName()).ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.fly.get.description")
    public static class Get {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(sender, arguments, "player");
            if (result.hasError()) {
                sender.sendMessage(result.getError().ct());
                return true;
            }

            Player target = result.getPlayer();

            sender.sendMessage(locale.ct("pipe.command.fly.target_allow_flight", "pipe.command.fly.target_not_allow_flight", target.getAllowFlight()).arg("player", target.getName()).ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player"));
        }

    }

}
