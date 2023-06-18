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
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

@MinecraftCommand(label = "invisible", aliases = {"invis"}, description = "pipe.command.invisible.description", arguments = {"player"})
public class InvisibleCommand {

    @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.invisible")
    public boolean execute(Command<CommandSender> command) {
        EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Invisible").ct());
            return true;
        }

        Collection<Player> targets = result.getEntities();
        targets.forEach(this::switchPlayer);

        if (targets.size() == 1) {
            command.sender().sendMessage(command.locale().ct("pipe.command.invisible.success.enabled", "pipe.command.invisible.success.disabled", result.first().hasPotionEffect(PotionEffectType.INVISIBILITY)).ph("player", result.first()).from("Invisible").ct());
        } else {
            command.sender().sendMessage(command.locale().t("pipe.command.invisible.success.all").from("Invisible").ct());
        }

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
        manager.addArgument(new BukkitPlayerArgument("player").allowFull().optional());
    }


    @CommandChild(label = "get", description = "pipe.command.invisible.get.description", arguments = {"player"})
    public static class Get {

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.invisible.get")
        public boolean execute(Command<CommandSender> command) {
            EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
            if (result.hasError()) {
                command.sender().sendMessage(result.getError().from("Invisible").ct());
                return true;
            }

            Player target = result.first();

            command.sender().sendMessage(command.locale().ct("pipe.command.invisible.target_invisible", "pipe.command.invisible.target_visible", target.hasPotionEffect(PotionEffectType.INVISIBILITY)).ph("player", target).from("Invisible").ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").allowRandom().allowSelf().allowNearest().optional());
        }

    }

}
