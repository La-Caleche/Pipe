package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
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
import org.bukkit.plugin.java.JavaPlugin;

@MinecraftCommand(label = "speed", description = "Speed command", arguments = {"speed", "player"})
public class SpeedCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (arguments.blank("player")) {
            if (sender instanceof Player player) {
                if (player.isFlying()) player.setFlySpeed(arguments.getFloat("speed"));
                else player.setWalkSpeed(arguments.getFloat("speed"));
                sender.sendMessage(locale.t("command.speed.set_%s.success".formatted(player.isFlying() ? "flying" : "walking")).ct());
            } else {
                sender.sendMessage(locale.t("command.speed.only_for_players").ct());
            }
        } else {
            Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
            if (target == null) {
                sender.sendMessage(locale.t("command.speed.player_not_found").arg("player", arguments.getString("player")).ct());
                return true;
            }
            if (target.isFlying()) target.setFlySpeed(arguments.getFloat("speed"));
            else target.setWalkSpeed(arguments.getFloat("speed"));
            sender.sendMessage(locale.t("command.speed.set_%s.success".formatted(target.isFlying() ? "flying" : "walking")).arg("player", target.getName()).ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new DoubleArgument("speed"));
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", description = "Get speed command", arguments = {"player"})
    public static class Get {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            if (arguments.blank("player")) {
                if (sender instanceof Player player) {
                    if (player.isFlying()) sender.sendMessage(locale.t("command.speed.get_flying.success").arg("speed", String.valueOf(player.getFlySpeed())).ct());
                    else sender.sendMessage(locale.t("command.speed.get_walking.success").arg("speed", String.valueOf(player.getWalkSpeed())).ct());
                } else {
                    sender.sendMessage(locale.t("command.speed.only_for_players").ct());
                }
            } else {
                Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
                if (target == null) {
                    sender.sendMessage(locale.t("command.speed.player_not_found").arg("player", arguments.getString("player")).ct());
                    return true;
                }
                if (target.isFlying()) sender.sendMessage(locale.t("command.speed.get_flying.success").arg("speed", String.valueOf(target.getFlySpeed())).arg("player", target.getName()).ct());
                else sender.sendMessage(locale.t("command.speed.get_walking.success").arg("speed", String.valueOf(target.getWalkSpeed())).arg("player", target.getName()).ct());
            }

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

    @CommandChild(label = "reset", description = "Reset speed command", arguments = {"player"})
    public static class Reset {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            if (arguments.blank("player")) {
                if (sender instanceof Player player) {
                    if (player.isFlying()) player.setFlySpeed(0.1F);
                    else player.setWalkSpeed(0.1F);
                    sender.sendMessage(locale.t("command.speed.reset_%s.success".formatted(player.isFlying() ? "flying" : "walking")).ct());
                } else {
                    sender.sendMessage(locale.t("command.speed.only_for_players").ct());
                }
            } else {
                Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
                if (target == null) {
                    sender.sendMessage(locale.t("command.speed.player_not_found").arg("player", arguments.getString("player")).ct());
                    return true;
                }
                if (target.isFlying()) target.setFlySpeed(0.1F);
                else target.setWalkSpeed(0.1F);
                sender.sendMessage(locale.t("command.speed.reset_%s.success".formatted(target.isFlying() ? "flying" : "walking")).arg("player", target.getName()).ct());
            }

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

}
