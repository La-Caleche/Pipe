package fr.lacaleche.pipe.proxy.utils;

import com.google.common.collect.Multimap;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ProxyCommandUtils {

    /**
     * This function allow MinecraftCommand to be recognized
     * as a global Bukkit Command. This allow users to use
     * tabcompletion in game.
     *
     * @param command the command to register as bukkit command
     * @since 1.0.0
     */
    public static void registerCommandAsBungee(MinecraftCommand command, Command proxyCommand) {
        Pipe<Plugin> pipe = Pipe.get();
        Plugin parent = pipe.getPlugin();

        try {
            PluginManager pluginManager = (PluginManager) parent.getProxy().getPluginManager();
            final Field bungeeCommandMap = pluginManager.getClass().getDeclaredField("commandMap");
            final Field commandByPlugin = pluginManager.getClass().getDeclaredField("commandsByPlugin");

            bungeeCommandMap.setAccessible(true);
            commandByPlugin.setAccessible(true);

            Map<String, Command> commandMap = (Map<String, Command>) bungeeCommandMap.get(pluginManager);
            Multimap<Plugin, Command> commandMultimap = (Multimap<Plugin, Command>) commandByPlugin.get(pluginManager);

            commandMap.put(command.label().toLowerCase(java.util.Locale.ROOT), proxyCommand);
            for (String alias : command.aliases()) {
                commandMap.put(alias.toLowerCase(java.util.Locale.ROOT), proxyCommand);
            }
            commandMultimap.put(parent, proxyCommand);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
        }
    }

    /**
     * TODO
     * */
    public static boolean isNativeCommand(String label) {
        Pipe<Plugin> pipe = Pipe.get();
        Plugin parent = pipe.getPlugin();

        try {
            PluginManager pluginManager = (PluginManager) parent.getProxy().getPluginManager();
            final Field bungeeCommandMap = pluginManager.getClass().getDeclaredField("commandMap");

            bungeeCommandMap.setAccessible(true);

            Map<String, Command> commandMap = (Map<String, Command>) bungeeCommandMap.get(pluginManager);

            return commandMap.containsKey(label.toLowerCase(java.util.Locale.ROOT));
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
            return false;
        }
    }

    /**
     * This function remove registered MinecraftCommand from
     * global Bukkit Command.
     *
     * @param command the command to be unregistered
     * @since 1.0.0
     */
    public static void unregisterBungeeCommand(MinecraftCommand command) {
        Pipe<Plugin> pipe = Pipe.get();
        Plugin parent = pipe.getPlugin();

        try {
            PluginManager pluginManager = (PluginManager) parent.getProxy().getPluginManager();
            final Field bungeeCommandMap = pluginManager.getClass().getDeclaredField("commandMap");
            final Field commandByPlugin = pluginManager.getClass().getDeclaredField("commandsByPlugin");

            bungeeCommandMap.setAccessible(true);
            commandByPlugin.setAccessible(true);

            Map<String, Command> commandMap = (Map<String, Command>) bungeeCommandMap.get(pluginManager);
            Multimap<Plugin, Command> commandMultimap = (Multimap<Plugin, Command>) commandByPlugin.get(pluginManager);

            commandMap.remove(command.label().toLowerCase(java.util.Locale.ROOT));
            for (String alias : command.aliases()) {
                commandMap.remove(alias.toLowerCase(java.util.Locale.ROOT));
            }
            commandMultimap.removeAll(parent);
        } catch (Exception exception) {
            SentryAPIImpl.getInstance().captureException(exception);
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
        Pipe<Plugin> pipe = Pipe.get();
        ProxyServer server = pipe.getPlugin().getProxy();

        for (CommandExecutor.Executor executors : executor.executor()) {
            if (executors == CommandExecutor.Executor.EVERYONE ||
                    (sender instanceof ProxiedPlayer && executors == CommandExecutor.Executor.PLAYER) ||
                    (sender == server.getConsole() && executors == CommandExecutor.Executor.SERVER))
                return true;
        }
        return false;
    }

    /**
     * TODO
     * */
    public static CommandExecutor.Executor translateSender(CommandSender sender) {
        Pipe<Plugin> pipe = Pipe.get();
        ProxyServer server = pipe.getPlugin().getProxy();

        if (sender instanceof ProxiedPlayer)
            return CommandExecutor.Executor.PLAYER;
        else if (sender == server.getConsole())
            return CommandExecutor.Executor.SERVER;
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
