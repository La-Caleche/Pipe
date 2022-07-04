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

@MinecraftCommand(label = "fly", description = "Fly command", arguments = {"player"})
public class FlyCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (arguments.blank("player")) {
            if (sender instanceof Player player) {
                player.setAllowFlight(!player.getAllowFlight());
                sender.sendMessage(locale.t("command.fly.success").ct());
            } else {
                sender.sendMessage(locale.t("command.fly.only_for_players").ct());
            }
        } else {
            Player target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));
            if (target == null) {
                sender.sendMessage(locale.t("command.fly.player_not_found").arg("player", arguments.getString("player")).ct());
                return true;
            }
            target.setAllowFlight(!target.getAllowFlight());
            sender.sendMessage(locale.t("command.fly.success").arg("player", target.getName()).ct());
        }

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

}
