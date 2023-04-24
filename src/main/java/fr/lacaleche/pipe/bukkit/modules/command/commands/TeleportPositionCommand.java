package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitWorldArgument;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.annotations.TabCompleter;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@MinecraftCommand(label = "teleportposition", aliases = {"tppos", "tpp", "tpposition", "teleportpos", "teleportp"}, description = "pipe.command.tpp.description", arguments = {"x", "y", "z", "yaw", "pitch", "world"})
public class TeleportPositionCommand {

    @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER, CommandExecutor.Executor.COMMAND_BLOCK}, minPermLevel = 20, permissions = "pipe.command.tpp")
    public boolean execute(Command<Player> command) {
        Player player = command.sender();
        Locale locale = command.locale();
        double x, y, z;
        float yaw, pitch;
        World world = player.getWorld();

        x = this.parse(command.args(), "x", player.getLocation().getX());
        y = this.parse(command.args(), "y", player.getLocation().getY());
        z = this.parse(command.args(), "z", player.getLocation().getZ());
        yaw = (float) this.parse(command.args(), "yaw", (double) player.getLocation().getYaw());
        pitch = (float) this.parse(command.args(), "pitch", (double) player.getLocation().getPitch());

        if (!command.args().blank("world")) {
            world = Pipe.getBukkit().<JavaPlugin>getPlugin().getServer().getWorld(command.args().getString("world"));

            if (world == null) {
                command.sender().sendMessage(locale.t("global.world_not_found").arg("world", command.args().getString("world")).from("Teleport").ct());
                return true;
            }
        }

        player.teleport(new Location(world, x, y, z, yaw, pitch));

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        command.sender().sendMessage(locale.t("pipe.command.tpp.success").arg("x", df.format(x)).arg("y", df.format(y)).arg("z", df.format(z)).arg("world", world.getName()).from("Teleport").ct());

        return true;
    }

    private double parse(Arguments arguments, String arg, Double def) {
        if (arguments.blank(arg) || arguments.getString(arg).equals("~")) {
            return def;
        }

        if (arguments.getString(arg).startsWith("~")) {
            return def + Double.parseDouble(arguments.getString(arg).substring(1));
        }

        return arguments.getDouble(arg);
    }

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new CustomArgument("x"));
        manager.addArgument(new CustomArgument("y"));
        manager.addArgument(new CustomArgument("z"));
        manager.addArgument(new CustomArgument("yaw").optional());
        manager.addArgument(new CustomArgument("pitch").optional());
        manager.addArgument(new BukkitWorldArgument("world").optional());
    }

    @TabCompleter
    public void complete(Completer completer) {
        if (completer.index() < 6)
            completer.add("~");

        if (completer.sender() instanceof Player player) {
            Location location = player.getLocation();
            switch (completer.index()) {
                case 1 -> completer.add(location.getX());
                case 2 -> completer.add(location.getY());
                case 3 -> completer.add(location.getZ());
                case 4 -> completer.add(location.getYaw());
                case 5 -> completer.add(location.getPitch());
            }
        }
    }

}
