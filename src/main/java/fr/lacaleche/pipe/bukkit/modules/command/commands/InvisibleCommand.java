package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@MinecraftCommand(label = "invisible", aliases = {"invis"}, description = "pipe.command.invisible.description", arguments = {"player"})
public class InvisibleCommand {

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Invisible").ct());
            return true;
        }

        Player target = result.getPlayer();

        this.switchPlayer(target);
        command.sender().sendMessage(command.locale().ct("pipe.command.invisible.success.invisible", "pipe.command.invisible.success.visible", target.hasPotionEffect(PotionEffectType.INVISIBILITY)).arg("player", target.getName()).from("Invisible").ct());

        return true;
    }

    private void switchPlayer(Player player) {
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.setCollidable(true);
        } else {
            player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
            player.setCollidable(false);
        }
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }


    @CommandChild(label = "get", description = "pipe.command.invisible.get.description", arguments = {"player"})
    public static class Get {

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Invisible").ct());
                return true;
            }

            Player target = result.getPlayer();

            command.sender().sendMessage(command.locale().ct("pipe.command.invisible.target_invisible", "pipe.command.invisible.target_visible", target.hasPotionEffect(PotionEffectType.INVISIBILITY)).arg("player", target.getName()).from("Invisible").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

}
