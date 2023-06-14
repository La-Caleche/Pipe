package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(label = "back", description = "pipe.command.back.description")
public class BackCommand {

    @CommandExecutor(minPermLevel = 20, executor = CommandExecutor.Executor.PLAYER)
    public boolean execute(Command<Player> command) {
        Client client = Pipe.getBukkit().getClient(command.sender());
        Location lastPlayerLocation = Pipe.getBukkit().getLastLocation(client);

        if (lastPlayerLocation == null) {
            command.sender().sendMessage(client.getLocale().t("pipe.command.back.no_location").ct());
            return true;
        }

        command.sender().teleport(lastPlayerLocation);
        command.sender().sendMessage(client.getLocale().t("pipe.command.back.success").ct());
        return true;
    }

}
