package fr.lacaleche.pipe.bukkit.utils;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilder;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PipeCommandUtils {

    /**
     * This function allow MinecraftCommand to be recognized
     * as a global Bukkit Command. This allow users to use
     * tabcompletion in game.
     *
     * @param command the command to register as bukkit command
     * @since 1.0.0
     */
    public static void registerCommandAsBukkit(JavaPlugin parent, MinecraftCommand command, BukkitCommand bukkitCommand) {
        try {
            Server server = (Server) parent.getServer();
            final Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(server);
            commandMap.register("nymbis", bukkitCommand);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    /**
     * TODO
     * */
    public static PlayerResult getPlayerFromArgsOrSender(CommandSender sender, Arguments arguments, String key) {
        Player target;
        Locale locale = Pipe.get().getDefaultLocale();

        if (sender instanceof Player player) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            locale = client.getLocale();
        }

        if (arguments.blank(key)) {
            if (sender instanceof Player player) target = player;
            else {
                return new PlayerResult(locale.t("global.only_for_players"));
            }
        } else target = Pipe.get().<JavaPlugin>getPlugin().getServer().getPlayer(arguments.getString("player"));

        if (target == null) {
            return new PlayerResult(locale.t("global.player_not_found").arg("player", arguments.getString("player")));
        }

        return new PlayerResult(target);
    }

    public static class PlayerResult {

        private Player player;
        private TranslationBuilder error;

        public PlayerResult(Player player) {
            this.player = player;
        }

        public PlayerResult(TranslationBuilder error) {
            this.error = error;
        }

        public Player getPlayer() {
            return player;
        }

        public TranslationBuilder getError() {
            return error;
        }

        public boolean hasError() {
            return error != null;
        }
    }

    /**
     * This function remove registered MinecraftCommand from
     * global Bukkit Command.
     *
     * @param command the command to be unregistered
     * @since 1.0.0
     */
    public static void unregisterBukkitCommand(JavaPlugin parent, MinecraftCommand command) {
        try {
            Server server = (Server) parent.getServer();
            final Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(server);
            commandMap.getKnownCommands().remove("nymbis:" + command.label());
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    /**
     * TODO
     * */

    public static boolean commandExist(JavaPlugin parent, String label) {
        try {
            Server server = parent.getServer();
            final Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(server);
            return commandMap.getKnownCommands().values().stream().anyMatch(command -> command.getName().equalsIgnoreCase(label) || command.getAliases().contains(label) || command.getLabel().equalsIgnoreCase(label));
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
            return false;
        }
    }


    /**
     * Check if command executor can be executed by
     * a sender. {@link CommandExecutor.Executor}
     *
     * @param executor executor of the command
     * @param sender   the instance of the command sender
     * @return true if the command can be executed by sender. Or false if not.
     * @since 1.0.0
     */
    public static boolean validateSender(CommandExecutor executor, CommandSender sender) {
        for (CommandExecutor.Executor executors : executor.executor()) {
            if (executors == CommandExecutor.Executor.EVERYONE ||
                    (sender instanceof Player && executors == CommandExecutor.Executor.PLAYER) ||
                    (sender instanceof ConsoleCommandSender && executors == CommandExecutor.Executor.SERVER) ||
                    (sender instanceof BlockCommandSender && executors == CommandExecutor.Executor.COMMAND_BLOCK))
                return true;
        }
        return false;
    }

    /**
     * TODO
     * */
    public static CommandExecutor.Executor translateSender(CommandSender sender) {
        if (sender instanceof Player)
            return CommandExecutor.Executor.PLAYER;
        else if (sender instanceof ConsoleCommandSender)
            return CommandExecutor.Executor.SERVER;
        else if (sender instanceof BlockCommandSender)
            return CommandExecutor.Executor.COMMAND_BLOCK;
        return null;
    }

    /**
     * Parse command help to sender if command not executed properly
     *
     * @param command the executed command
     * @param sender  the instance of the command sender
     * @since 1.0.0
     */
    public static void parseHelpToSender(Class<?> command, CommandSender sender) {
        sender.sendMessage("(missing HelpUtils)");
    }

    private static String joinExecutors(CommandExecutor.Executor[] executors) {
        return Arrays.stream(executors)
                .map(executor -> StringUtils.capitalize(executor.name().replace('_', ' ')))
                .collect(Collectors.joining(","));
    }

}
