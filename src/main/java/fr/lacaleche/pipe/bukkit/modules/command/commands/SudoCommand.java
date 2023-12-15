package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.modules.command.utils.BukkitEntitySelector;
import fr.lacaleche.pipe.bukkit.modules.command.utils.BukkitSudoUtils;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.commands.utils.EntitySelectorResult;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.network.chat.PlayerChatMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "sudo", description = "pipe.command.sudo.description", arguments = {"player", "command"})
public class SudoCommand {

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new BukkitPlayerArgument("player").allowSelf().allowRandom().allowNearest());
        manager.addArgument(new StringArgument("command").setMultiple(true));
    }

    @CommandExecutor(permissions = "pipe.command.sudo", minPermLevel = 100)
    public boolean execute(Command<CommandSender> command) {
        String commandString = command.args().getString("command");
        EntitySelectorResult<Player> result = BukkitEntitySelector.parsePlayers(command, "player");
        if (result.hasError()) {
            command.sender().sendMessage(result.getError().from("Sudo").ct());
            return true;
        }

        Player target = result.first();
        target.chat(commandString);
        command.sender().sendMessage(command.locale().t("pipe.command.sudo.sended").ph("player", target).arg("command", commandString).from("Sudo").ct());

        return true;
    }

}
