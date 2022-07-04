package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@MinecraftCommand(label = "nightvision", aliases = {"nv"}, description = "Night vision command", arguments = {"player"})
public class NightVisionCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (arguments.blank("player")) {
            if (sender instanceof Player player) {
                player.setPlayerTime(player.getPlayerTime() == 0 ? 1 : 0, false);
                sender.sendMessage(locale.t("command.nightvision.success").ct());
            } else {
                sender.sendMessage(locale.t("command.nightvision.only_for_players").ct());
            }
        } else {
            Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
            if (target == null) {
                sender.sendMessage(locale.t("command.nightvision.player_not_found").arg("player", arguments.getString("player")).ct());
                return true;
            }
            target.setPlayerTime(target.getPlayerTime() == 0 ? 1 : 0, false);
            sender.sendMessage(locale.t("command.nightvision.success").arg("player", target.getName()).ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

}
