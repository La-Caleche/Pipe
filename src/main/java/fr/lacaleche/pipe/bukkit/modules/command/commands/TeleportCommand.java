package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.modules.command.utils.BukkitEntitySelector;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.utils.EntitySelectorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

@MinecraftCommand(label = "teleport", aliases = {"tp"}, description = "pipe.command.tp.description", arguments = {"left_player", "right_player"})
public class TeleportCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.tp")
    public boolean execute(Command<CommandSender> command) {
        Player leftPlayer = null, rightPlayer;
        EntitySelectorResult<Player> leftPlayerResult = null, rightPlayerResult;

        if (command.args().blank("rightPlayer")) {
            if (!(command.sender() instanceof Player playerSender)) {
                command.sender().sendMessage(command.locale().t("global.only_for_players").from("Teleport").ct());
                return true;
            }

            leftPlayer = playerSender;
            rightPlayerResult = BukkitEntitySelector.parsePlayers(command, "leftPlayer");
        } else {
            leftPlayerResult = BukkitEntitySelector.parsePlayers(command, "leftPlayer");
            rightPlayerResult = BukkitEntitySelector.parsePlayers(command, "rightPlayer");
        }

        if (leftPlayerResult != null && leftPlayerResult.hasError()) {
            command.sender().sendMessage(leftPlayerResult.getError().from("Teleport").ct());
            return true;
        }

        if (rightPlayerResult.hasError()) {
            command.sender().sendMessage(rightPlayerResult.getError().from("Teleport").ct());
            return true;
        }

        rightPlayer = rightPlayerResult.first();

        if (leftPlayerResult == null || leftPlayerResult.size() == 1) {
            if (leftPlayerResult != null) leftPlayer = leftPlayerResult.first();

            leftPlayer.teleport(rightPlayer.getLocation().clone().add(0, 1, 0));
            command.sender().sendMessage(command.locale().t("pipe.command.tp.success").ph("left_player", leftPlayer).ph("right_player", rightPlayer).from("Teleport").ct());
            return true;
        }

        leftPlayerResult.getEntities().forEach(player -> player.teleport(rightPlayer.getLocation().clone().add(0, 1, 0)));
        command.sender().sendMessage(command.locale().t("pipe.command.tp.success.all").ph("right_player", rightPlayer).from("Teleport").ct());
        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("leftPlayer").allowFull());
        manager.addArgument(new BukkitPlayerArgument("rightPlayer").fullButAll().optional());
    }

}
