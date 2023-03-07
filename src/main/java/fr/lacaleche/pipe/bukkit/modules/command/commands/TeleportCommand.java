package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@MinecraftCommand(label = "teleport", aliases = {"tp"}, description = "pipe.command.tp.description", arguments = {"player", "otherPlayer"})
public class TeleportCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.tp")
    public boolean execute(Command<CommandSender> command) {
        Player player, otherPlayer;
        String playerArg, otherPlayerArg;

        if (command.args().blank("otherPlayer")) {
            if (command.sender() instanceof Player playerSender) player = playerSender;
            else {
                command.sender().sendMessage(command.locale().t("global.only_for_players").from("Teleport").ct());
                return true;
            }
            playerArg = command.sender().getName();
            otherPlayer = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer((otherPlayerArg = command.args().getString("player")));
        } else {
            player = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer((playerArg = command.args().getString("player")));
            otherPlayer = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer((otherPlayerArg = command.args().getString("otherPlayer")));
        }

        if (player == null) {
            command.sender().sendMessage(command.locale().t("global.player_not_found").arg("player", playerArg).from("Teleport").ct());
            return true;
        }

        if (otherPlayer == null) {
            command.sender().sendMessage(command.locale().t("global.player_not_found").arg("player", otherPlayerArg).from("Teleport").ct());
            return true;
        }

        player.teleport(otherPlayer.getLocation().add(0, 1, 0));
        command.sender().sendMessage(command.locale().t("pipe.command.tp.success").arg("player", player.getName()).arg("otherPlayer", otherPlayer.getName()).from("Teleport").ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player"));
        manager.addArgument(new BukkitPlayerArgument("otherPlayer").optional());
    }

}
