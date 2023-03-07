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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@MinecraftCommand(label = "nightvision", aliases = {"nv"}, description = "pipe.command.nightvision.description", arguments = {"player"})
public class NightVisionCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.nightvision")
    public boolean execute(Command<CommandSender> command) {
        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Night Vision").ct());
            return true;
        }

        Player target = result.getPlayer();

        command.sender().sendMessage(target.getPlayerTime() + "");

        nightPlayer(target);
        command.sender().sendMessage(command.locale().ct("pipe.command.nightvision.success.enabled", "pipe.command.nightvision.success.disabled", target.hasPotionEffect(PotionEffectType.NIGHT_VISION)).arg("player", target.getName()).from("Night Vision").ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.nightvision.get.description")
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.nightvision.get")
        public boolean execute(Command<CommandSender> command) {
            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(command.sender(), command.args(), "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Night Vision").ct());
                return true;
            }

            Player target = result.getPlayer();

            nightPlayer(target);
            command.sender().sendMessage(command.locale().ct("pipe.command.nightvision.target_enabled", "pipe.command.nightvision.target_disabled", target.getPlayerTime() == 0).arg("player", target.getName()).from("Night Vision").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }
    }

    private static void nightPlayer(Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        else
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 20, false, false));
    }

}
