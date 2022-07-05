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

@MinecraftCommand(label = "nightvision", aliases = {"nv"}, description = "pipe.command.nightvision.description", arguments = {"player"})
public class NightVisionCommand {

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

        target.setPlayerTime(target.getPlayerTime() == 0 ? 1 : 0, false);
        sender.sendMessage(locale.ct("pipe.command.nightvision.success.enabled", "pipe.command.nightvision.success.disabled", target.getPlayerTime() == 0).ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.nightvision.get.description")
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

            sender.sendMessage(locale.ct("pipe.command.nightvision.target_enabled", "pipe.command.nightvision.target_disabled", target.getPlayerTime() == 0).arg("player", target.getName()).ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

}
