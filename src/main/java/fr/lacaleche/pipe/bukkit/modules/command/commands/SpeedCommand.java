package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.DoubleArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "speed", description = "pipe.command.speed.description", arguments = {"speed", "player"})
public class SpeedCommand {

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

        if (target.isFlying()) target.setFlySpeed(arguments.getFloat("speed"));
        else target.setWalkSpeed(arguments.getFloat("speed"));
        sender.sendMessage(locale.ct("pipe.command.speed.success.enabled", "pipe.command.speed.success.disabled", target.isFlying()).ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new DoubleArgument("speed"));
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", description = "pipe.command.speed.get.description", arguments = {"player"})
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

            sender.sendMessage(locale.ct("pipe.command.speed.target_speed.flying", "pipe.command.speed.target_speed.walking", target.isFlying()).arg("fly_speed", target.getFlySpeed()).arg("walk_speed", target.getWalkSpeed()).ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

    @CommandChild(label = "reset", description = "pipe.command.speed.reset.description", arguments = {"player"})
    public static class Reset {

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

            if (target.isFlying()) target.setFlySpeed(0.1F);
            else target.setWalkSpeed(0.1F);
            sender.sendMessage(locale.ct("pipe.command.speed.reset.flying", "pipe.command.speed.reset.walking", target.isFlying()).arg("player", target.getName()).ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

}
