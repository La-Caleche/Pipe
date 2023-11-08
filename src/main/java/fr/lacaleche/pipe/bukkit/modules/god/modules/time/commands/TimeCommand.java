package fr.lacaleche.pipe.bukkit.modules.god.modules.time.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitWorldArgument;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.IntegerArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@MinecraftCommand(label = "world", description = "pipe.command.time.description")
public class TimeCommand {

    @CommandChild(label = "get", description = "pipe.command.time.get.description")
    public static class Get {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitWorldArgument("world").optional());
        }

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.time.get")
        public boolean execute(Command<CommandSender> command) {
            Plugin plugin = Pipe.getBukkit().getPlugin();

            if (command.args().blank("world")) {
                plugin.getServer().getWorlds().forEach(world -> {
                    command.sender().sendMessage(command.locale().t("pipe.command.time.get").arg("world", world.getName()).arg("time", world.getTime()).arg("full_time", world.getFullTime()).from("Time").ct());
                });
                return true;
            }

            World world = plugin.getServer().getWorld(command.args().getString("world"));
            if (world == null) {
                command.sender().sendMessage(command.locale().t("pipe.command.time.world_not_found").arg("world", command.args().getString("world")).from("Time").ct());
                return true;
            }

            command.sender().sendMessage(command.locale().t("pipe.command.time.get").arg("world", world.getName()).arg("time", world.getTime()).arg("full_time", world.getFullTime()).from("Time").ct());

            return true;
        }

    }

    @CommandChild(label = "set", description = "pipe.command.time.set.description")
    public static class Set {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new IntegerArgument("time").min(0).max(24000).step(1000));
            manager.addArgument(new BukkitWorldArgument("world").optional());
        }

        @CommandExecutor(minPermLevel = 20, permissions = "pipe.command.time.set")
        public boolean execute(Command<CommandSender> command) {
            Plugin plugin = Pipe.getBukkit().getPlugin();

            if (command.args().blank("world")) {
                if (!(command.sender() instanceof Player player)) {
                    command.sender().sendMessage(command.locale().t("pipe.command.time.player_only").from("Time").ct());
                    return true;
                }

                command.sender().sendMessage(command.locale().t("pipe.command.time.set").arg("world", player.getWorld().getName()).arg("time", player.getWorld().getTime()).arg("full_time", player.getWorld().getFullTime()).from("Time").ct());
                return true;
            }

            World world = plugin.getServer().getWorld(command.args().getString("world"));
            if (world == null) {
                command.sender().sendMessage(command.locale().t("pipe.command.time.world_not_found").arg("world", command.args().getString("world")).from("Time").ct());
                return true;
            }

            command.sender().sendMessage(command.locale().t("pipe.command.time.set").arg("world", world.getName()).arg("time", world.getTime()).arg("full_time", world.getFullTime()).from("Time").ct());

            return true;
        }

    }

}
