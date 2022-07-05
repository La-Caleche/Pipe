package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "heal", description = "pipe.command.heal.description", arguments = {"player"})
public class HealCommand {

    @CommandExecutor
    public boolean execute(CommandSender sender, Arguments arguments) {
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(sender, arguments, "player");
        if (result.hasError()) {
            sender.sendMessage(result.getError().ct());
            return true;
        }

        Player target = result.getPlayer();

        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        sender.sendMessage(locale.t("pipe.command.heal.success").arg("player", target.getName()).ct());

        return true;
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").optional());
    }

    @CommandChild(label = "get", arguments = {"player"}, description = "pipe.command.heal.get.description")
    public static class Get {

        @CommandExecutor
        public boolean execute(CommandSender sender, Arguments arguments) {
            Locale locale = Pipe.get().getDefaultLocale();

            if (sender instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                locale = client.getLocale();
            }

            PipeCommandUtils.PlayerResult result = PipeCommandUtils.getPlayerFromArgsOrSender(sender, arguments, "player");
            if (result.hasError()) {
                sender.sendMessage(result.getError().ct());
                return true;
            }

            Player target = result.getPlayer();

            sender.sendMessage(locale.t("pipe.command.heal.get_health").arg("player", target.getName()).arg("health", target.getHealth()).arg("max_health", target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()).ct());

            return true;
        }

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player"));
        }

    }

}
