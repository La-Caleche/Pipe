package fr.lacaleche.pipe.bukkit.modules.god.modules.world.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@MinecraftCommand(label = "world", description = "pipe.command.world.description")
public class WorldCommand {

    @CommandChild(label = "time", description = "pipe.command.world.time.description")
    public static class Time {

        @CommandChild(label = "get", description = "pipe.command.world.time.get.description")
        public static class Get {

            @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.world.time.get")
            public boolean execute(Command<CommandSender> command) {
                Plugin plugin = Pipe.getBukkit().getPlugin();
                plugin.getServer().getWorlds().forEach(world -> {
                    command.sender().sendMessage(command.locale().t("pipe.command.world.time.get").arg("world", world.getName()).arg("time", world.getTime()).arg("full_time", world.getFullTime()).from("World").ct());
                });
                return true;
            }

        }

    }

}
