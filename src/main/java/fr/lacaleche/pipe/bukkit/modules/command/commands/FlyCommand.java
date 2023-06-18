package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.modules.command.utils.BukkitEntitySelector;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.utils.EntitySelectorResult;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

@MinecraftCommand(label = "fly", description = "pipe.command.fly.description", arguments = {"player"})
public class FlyCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.fly")
    public boolean execute(Command<CommandSender> command) {
        EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Fly").ct());
            return true;
        }

        Collection<Player> targets = result.getEntities();
        targets.forEach(target -> target.setAllowFlight(!target.getAllowFlight()));

        if (targets.size() == 1) {
            command.sender().sendMessage(command.locale().ct("pipe.command.fly.success.enabled", "pipe.command.fly.success.disabled", result.first().getAllowFlight()).ph("player", result.first()).from("Fly").ct());
        } else {
            command.sender().sendMessage(command.locale().t("pipe.command.fly.success.all").from("Fly").ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.fly.get.description")
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.fly.get")
        public boolean execute(Command<CommandSender> command) {
            EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Fly").ct());
                return true;
            }

            Player target = result.first();

            command.sender().sendMessage(command.locale().ct("pipe.command.fly.target_allow_flight", "pipe.command.fly.target_not_allow_flight", target.getAllowFlight()).ph("player", target).from("Fly").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest());
        }

    }

}
