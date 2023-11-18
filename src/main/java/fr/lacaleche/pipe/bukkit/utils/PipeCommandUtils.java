package fr.lacaleche.pipe.bukkit.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
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
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PipeCommandUtils {

    /**
     * This function allow MinecraftCommand to be recognized
     * as a global Bukkit Command. This allow users to use
     * tabcompletion in game.
     *
     * @param command the command to register as bukkit command
     * @since 1.0.0
     */
    public static void registerCommandAsBukkit(Plugin parent, MinecraftCommand command, BukkitCommand bukkitCommand) {
        try {
            Server server = parent.getServer();
            final Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(server);
            commandMap.register("nymbis", bukkitCommand);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    /**
     * This function remove registered MinecraftCommand from
     * global Bukkit Command.
     *
     * @param command the command to be unregistered
     * @since 1.0.0
     */
    public static void unregisterBukkitCommand(Plugin parent, MinecraftCommand command) {
        try {
            Server server = (Server) parent.getServer();
            final Field bukkitCommandMap = server.getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(server);

            HelpMap helpMap = server.getHelpMap();
            final Field helpTopics = helpMap.getClass().getDeclaredField("helpTopics");
            helpTopics.setAccessible(true);
            Map<String, HelpTopic> topics = (Map<String, HelpTopic>) helpTopics.get(helpMap);

            removeCommandLabel(commandMap, topics, command.label());
            Arrays.stream(command.aliases()).forEach(alias -> removeCommandLabel(commandMap, topics, alias));
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    private static void removeCommandLabel(CommandMap commandMap, Map<String, HelpTopic> topics, String label) {
        commandMap.getKnownCommands().remove(label);
        commandMap.getKnownCommands().remove("nymbis:" + label);
        topics.remove("/" + label);
        topics.remove("/nymbis:" + label);
    }

    /**
     * TODO
     * */

    public static boolean commandExist(Plugin parent, String cmd) {
        if (cmd.matches(".+:.+"))
            cmd = cmd.replace(Iterables.get(Splitter.on(':').split(cmd), 0) + ":", "");
        cmd = "/" + cmd;
        return parent.getServer().getHelpMap().getHelpTopics().stream().map(HelpTopic::getName).toList().contains(cmd);
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
        List<CommandExecutor.Executor> executors = Arrays.stream(executor.executors()).toList();
        if (executors.contains(CommandExecutor.Executor.EVERYONE)) return true;
        if (sender instanceof Player && executors.contains(CommandExecutor.Executor.PLAYER)) return true;
        if (sender instanceof ConsoleCommandSender && executors.contains(CommandExecutor.Executor.SERVER)) return true;
        return sender instanceof BlockCommandSender && executors.contains(CommandExecutor.Executor.COMMAND_BLOCK);
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

}
