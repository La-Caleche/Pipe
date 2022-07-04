package fr.lacaleche.pipe.bukkit.modules.command.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitWorldArgument;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.annotations.TabCompleter;
import fr.lacaleche.pipe.common.commands.argument.arguments.CustomArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.DoubleArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.FloatArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@MinecraftCommand(label = "teleportposition", aliases = {"tppos", "tpp", "tpposition", "teleportpos", "teleportp"}, description = "Tp pos command", arguments = {"x", "y", "z", "yaw", "pitch", "world"})
public class TeleportPositionCommand {

    @CommandExecutor(executor = {CommandExecutor.Executor.PLAYER, CommandExecutor.Executor.COMMAND_BLOCK})
    public boolean execute(CommandSender sender, Arguments arguments) {
        Player player = (Player) sender;
        Client client = Pipe.get().getClient(player.getUniqueId());
        Locale locale = client.getLocale();
        double x, y, z;
        float yaw, pitch;
        World world = player.getWorld();

        x = this.parse(arguments, "x", player.getLocation().getX());
        y = this.parse(arguments, "y", player.getLocation().getY());
        z = this.parse(arguments, "z", player.getLocation().getZ());
        yaw = (float) this.parse(arguments, "yaw", (double) player.getLocation().getYaw());
        pitch = (float) this.parse(arguments, "pitch", (double) player.getLocation().getPitch());

        if (!arguments.blank("world")) {
            world = Pipe.get().<JavaPlugin>getPlugin().getServer().getWorld(arguments.getString("world"));

            if (world == null) {
                sender.sendMessage(locale.t("command.tp.world_not_found").arg("world", arguments.getString("world")).ct());
                return true;
            }
        }

        player.teleport(new Location(world, x, y, z, yaw, pitch));
        sender.sendMessage(locale.t("command.tp.success").ct());

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
                case 1 -> completer.add(String.valueOf(location.getX()));
                case 2 -> completer.add(String.valueOf(location.getY()));
                case 3 -> completer.add(String.valueOf(location.getZ()));
                case 4 -> completer.add(String.valueOf(location.getYaw()));
                case 5 -> completer.add(String.valueOf(location.getPitch()));
            }
        }
    }

}
