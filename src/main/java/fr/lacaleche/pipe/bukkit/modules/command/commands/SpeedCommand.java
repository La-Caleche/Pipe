package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.IntegerArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

@MinecraftCommand(label = "speed", description = "pipe.command.speed.description", arguments = {"speed", "player"})
public class SpeedCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.speed")
    public boolean execute(Command<CommandSender> command) {
        PipeCommandUtils.PlayerResult result = PipeCommandUtils.parseSelector(command.sender(), command.args(), "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Speed").ct());
            return true;
        }

        Collection<Player> targets = result.getPlayers();
        int speed = command.args().getInt("speed");

        if (speed < 0 || speed > 10) {
            command.sender().sendMessage(command.locale().t("pipe.command.speed.invalid_speed").from("Speed").ct());
            return true;
        }

        targets.forEach(target -> {
            if (target.isFlying()) target.setFlySpeed(speed / 10f);
            else target.setWalkSpeed(speed / 10f);
        });

        if (targets.size() == 1) {
            command.sender().sendMessage(command.locale().ct("pipe.command.speed.success.fly", "pipe.command.speed.success.walk", result.getPlayer().isFlying()).arg("speed", speed).arg("player", result.getPlayer().getName()).from("Speed").ct());
        } else {
            command.sender().sendMessage(command.locale().t("pipe.command.speed.success.all").from("Speed").ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new IntegerArgument("speed"));
        manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
    }

    @CommandChild(label = "get", description = "pipe.command.speed.get.description", arguments = {"player"})
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.speed.get")
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.parseSelector(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Speed").ct());
                return true;
            }

            Player target = result.getPlayer();

            int flySpeed = (int) (target.getFlySpeed() * 10);
            int walkSpeed = (int) (target.getWalkSpeed() * 10);

            command.sender().sendMessage(command.locale().t("pipe.command.speed.target_speed").arg("player", target.getName()).arg("fly_speed", flySpeed).arg("walk_speed", walkSpeed).from("Speed").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest().optional());
        }

    }

    @CommandChild(label = "reset", description = "pipe.command.speed.reset.description", arguments = {"player"})
    public static class Reset {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.speed.reset")
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.parseSelector(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Speed").ct());
                return true;
            }

            Collection<Player> targets = result.getPlayers();

            targets.forEach(target -> {
                if (target.isFlying()) target.setFlySpeed(0.1F);
                else target.setWalkSpeed(0.2F);
            });

            if (targets.size() == 1) {
                command.sender().sendMessage(command.locale().ct("pipe.command.speed.reset.flying", "pipe.command.speed.reset.walking", result.getPlayer().isFlying()).arg("player", result.getPlayer().getName()).from("Speed").ct());
            } else {
                command.sender().sendMessage(command.locale().t("pipe.command.speed.reset.all").from("Speed").ct());
            }

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
        }

    }

}
