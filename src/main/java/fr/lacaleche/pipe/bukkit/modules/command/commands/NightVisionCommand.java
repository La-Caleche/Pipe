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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

@MinecraftCommand(label = "nightvision", aliases = {"nv"}, description = "pipe.command.nightvision.description", arguments = {"player"})
public class NightVisionCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.nightvision")
    public boolean execute(Command<CommandSender> command) {
        EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Night Vision").ct());
            return true;
        }

        Collection<Player> targets = result.getEntities();
        targets.forEach(this::nightPlayer);

        if (targets.size() == 1) {
            command.sender().sendMessage(command.locale().ct("pipe.command.nightvision.success.enabled", "pipe.command.nightvision.success.disabled", result.first().hasPotionEffect(PotionEffectType.NIGHT_VISION)).ph("player", result.first()).from("Night Vision").ct());
        } else {
            command.sender().sendMessage(command.locale().t("pipe.command.nightvision.success.all").from("Night Vision").ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.nightvision.get.description")
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.nightvision.get")
        public boolean execute(Command<CommandSender> command) {
            EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Night Vision").ct());
                return true;
            }

            Player target = result.first();

            command.sender().sendMessage(command.locale().ct("pipe.command.nightvision.target_enabled", "pipe.command.nightvision.target_disabled", target.hasPotionEffect(PotionEffectType.NIGHT_VISION)).ph("player", target).from("Night Vision").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest().optional());
        }
    }

    private void nightPlayer(Player player) {
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        else
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 20, false, false));
    }

}
