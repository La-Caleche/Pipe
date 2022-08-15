package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.DoubleArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "speed", description = "pipe.command.speed.description", arguments = {"speed", "player"})
public class SpeedCommand {

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Speed").ct());
            return true;
        }

        Player target = result.getPlayer();

        if (target.isFlying()) target.setFlySpeed(command.args().getFloat("speed"));
        else target.setWalkSpeed(command.args().getFloat("speed"));
        command.sender().sendMessage(command.locale().ct("pipe.command.speed.success.fly", "pipe.command.speed.success.walk", target.isFlying()).arg("speed", command.args().getFloat("speed")).arg("player", target.getName()).from("Speed").ct());

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
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Speed").ct());
                return true;
            }

            Player target = result.getPlayer();

            command.sender().sendMessage(command.locale().t("pipe.command.speed.target_speed").arg("player", target.getName()).arg("fly_speed", target.getFlySpeed()).arg("walk_speed", target.getWalkSpeed()).from("Speed").ct());

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
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Speed").ct());
                return true;
            }

            Player target = result.getPlayer();

            if (target.isFlying()) target.setFlySpeed(0.1F);
            else target.setWalkSpeed(0.2F);
            command.sender().sendMessage(command.locale().ct("pipe.command.speed.reset.flying", "pipe.command.speed.reset.walking", target.isFlying()).arg("player", target.getName()).from("Speed").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

}
