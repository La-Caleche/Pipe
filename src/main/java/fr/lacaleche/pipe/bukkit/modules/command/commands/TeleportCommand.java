package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@MinecraftCommand(label = "teleport", aliases = {"tp"}, description = "pipe.command.tp.description", arguments = {"player", "otherPlayer"})
public class TeleportCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        Player player, otherPlayer;

        if (arguments.blank("otherPlayer")) {
            if (sender instanceof Player playerSender) player = playerSender;
            else {
                sender.sendMessage(locale.t("global.only_for_players").ct());
                return true;
            }
            otherPlayer = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
        } else {
            player = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
            otherPlayer = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("otherPlayer"));
        }

        if (player == null) {
            sender.sendMessage(locale.t("global.player_not_found").arg("player", arguments.getString("player")).ct());
            return true;
        }

        if (otherPlayer == null) {
            sender.sendMessage(locale.t("global.player_not_found").arg("player", arguments.getString("otherPlayer")).ct());
            return true;
        }

        player.teleport(otherPlayer.getLocation().add(0, 1, 0));
        sender.sendMessage(locale.t("pipe.command.tp.success").arg("player", player.getName()).arg("otherPlayer", otherPlayer.getName()).ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player"));
        manager.addArgument(new BukkitPlayerArgument("otherPlayer").optional());
    }

}
