package fr.lacaleche.pipe.proxy.utils;

import com.google.common.collect.Multimap;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.proxy.ProxyPlugin;
import net.kyori.adventure.text.Component;
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
    public static void registerCommandAsBungee(MinecraftCommand command, SimpleCommand proxyCommand) {
        Pipe pipe = Pipe.get();
        ProxyServer proxy = pipe.<ProxyPlugin>getPlugin().getServer();
        CommandManager commandManager = proxy.getCommandManager();
        proxy.getCommandManager().register(command.label(), proxyCommand, command.aliases());
    }

    /**
     * This function remove registered MinecraftCommand from
     * global Bukkit Command.
     *
     * @param command the command to be unregistered
     * @since 1.0.0
     */
    public static void unregisterBungeeCommand(MinecraftCommand command) {
        Pipe pipe = Pipe.get();
        ProxyServer proxy = pipe.<ProxyPlugin>getPlugin().getServer();
        CommandManager commandManager = proxy.getCommandManager();

        commandManager.unregister(command.label());
    }

    /**
     * Check if command executor can be executed by
     * a sender. {@link CommandExecutor.Executor}
     *
     * @param executor executor of the command
     * @param source   the instance of the command source
     * @return true if the command can be executed by sender. Or false if not.
     * @since 1.0.0
     */
    public static boolean validateSender(CommandExecutor executor, CommandSource source) {
        Pipe pipe = Pipe.get();
        ProxyServer server = pipe.<ProxyPlugin>getPlugin().getServer();

        for (CommandExecutor.Executor executors : executor.executor()) {
            if (executors == CommandExecutor.Executor.EVERYONE ||
                    (source instanceof Player && executors == CommandExecutor.Executor.PLAYER) ||
                    (source instanceof ConsoleCommandSource && executors == CommandExecutor.Executor.SERVER))
                return true;
        }
        return false;
    }

    /**
     * TODO
     * */
    public static boolean commandExist(String label) {
        Pipe pipe = Pipe.get();
        ProxyServer proxy = pipe.<ProxyPlugin>getPlugin().getServer();
        CommandManager commandManager = proxy.getCommandManager();
        return commandManager.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(label));
    }

    /**
     * TODO
     * */
    public static CommandExecutor.Executor translateSender(CommandSource source) {
        Pipe pipe = Pipe.get();
        ProxyServer server = pipe.<ProxyPlugin>getPlugin().getServer();

        if (source instanceof Player)
            return CommandExecutor.Executor.PLAYER;
        else if (source instanceof ConsoleCommandSource)
            return CommandExecutor.Executor.SERVER;
        return null;
    }

    /**
     * Parse command help to sender if command not executed properly
     *
     * @param command the executed command
     * @param source  the instance of the command source
     * @since 1.0.0
     */
    public static void parseHelpToSender(Class<?> command, CommandSource source) {
        source.sendMessage(Component.text("(missing helputils proxy)"));
    }

    private static String joinExecutors(CommandExecutor.Executor[] executors) {
        return Arrays.stream(executors)
                .map(executor -> StringUtils.capitalize(executor.name().replace('_', ' ')))
                .collect(Collectors.joining(","));
    }

}
