package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "heal", description = "pipe.command.heal.description", arguments = {"player"})
public class HealCommand {

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Heal").ct());
            return true;
        }

        Player target = result.getPlayer();

        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        command.sender().sendMessage(command.locale().t("pipe.command.heal.success").arg("player", target.getName()).from("Heal").ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.heal.get.description")
    public static class Get {

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Heal").ct());
                return true;
            }

            Player target = result.getPlayer();

            command.sender().sendMessage(command.locale().t("pipe.command.heal.get_health").arg("player", target.getName()).arg("health", target.getHealth()).arg("max_health", target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).from("Heal").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player"));
        }

    }

}
