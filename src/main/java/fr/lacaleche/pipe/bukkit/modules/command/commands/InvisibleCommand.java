package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

@MinecraftCommand(label = "invisible", aliases = {"invis"}, description = "Invisible command", arguments = {"player"})
public class InvisibleCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (arguments.blank("player")) {
            if (sender instanceof Player player) {
                this.switchPlayer(player);
                sender.sendMessage(locale.t("command.invisible.success_%s".formatted(player.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "invisible" : "visible")).ct());
            } else {
                sender.sendMessage(locale.t("command.invisible.only_for_players").ct());
            }
        } else {
            Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
            if (target == null) {
                sender.sendMessage(locale.t("command.invisible.player_not_found").arg("player", arguments.getString("player")).ct());
                return true;
            }
            this.switchPlayer(target);
            sender.sendMessage(locale.t("command.invisible.success_%s".formatted(target.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "invisible" : "visible")).arg("player", target.getName()).ct());
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
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }


    @CommandChild(label = "get", description = "Get if invisible command", arguments = {"player"})
    public static class Get {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            if (arguments.blank("player")) {
                if (sender instanceof Player player) {
                    sender.sendMessage(locale.t("command.invisible.is_%s".formatted(player.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "invisible" : "visible")).ct());
                } else {
                    sender.sendMessage(locale.t("command.invisible.only_for_players").ct());
                }
            } else {
                Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
                if (target == null) {
                    sender.sendMessage(locale.t("command.invisible.player_not_found").arg("player", arguments.getString("player")).ct());
                    return true;
                }
                sender.sendMessage(locale.t("command.invisible.is_%s".formatted(target.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "invisible" : "visible")).arg("player", target.getName()).ct());
            }

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player").optional());
        }

    }

}
