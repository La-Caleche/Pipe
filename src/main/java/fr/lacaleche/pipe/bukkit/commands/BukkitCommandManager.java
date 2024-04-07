package fr.lacaleche.pipe.bukkit.commands;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.GlobalCommandManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.commands.helper.interfaces.Helper;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BukkitCommandManager extends GlobalCommandManager {

    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        BukkitPipe pipe = Pipe.getBukkit();

        MinecraftCommand command = super.registerNewCommand(module, newCommand);
        this.registerCommandAsBukkit(pipe.getPlugin(), command, new SimpleBukkitPipeCommand(command));
        return command;
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        Logger.customDebug("Unregistering command " + unregistered.getSimpleName() + " from module " + module.getClass().getName());
        BukkitPipe pipe = Pipe.getBukkit();

        if(!super.unregisterCommand(module, unregistered)) return false;
        this.unregisterBukkitCommand(pipe.getPlugin(), CommandsUtils.validateCommand(unregistered));
        return true;
    }

    @Override
    public void unregisterModuleCommands(IModule module) {
        List<Class<MinecraftCommand>> commands = this.getModuleCommands(module);
        if (commands == null || commands.isEmpty()) return;
        List<Class<MinecraftCommand>> copy = new ArrayList<>(commands);

        for (Class<MinecraftCommand> command : copy) {
            this.unregisterCommand(module, command);
        }
    }

    @Override
    public CommandExecutor.Executor translateSender(Object object) {
        if (!(object instanceof CommandSender sender)) return null;

        if (sender instanceof Player)
            return CommandExecutor.Executor.PLAYER;
        else if (sender instanceof ConsoleCommandSender)
            return CommandExecutor.Executor.SERVER;
        else if (sender instanceof BlockCommandSender)
            return CommandExecutor.Executor.COMMAND_BLOCK;

        return null;
    }

    @Override
    public Client getClient(Object sender) {
        if (!(sender instanceof Player)) return null;
        return Pipe.getBukkit().getClient(((Player) sender).getUniqueId());
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
    @Override
    public boolean validateSender(CommandExecutor executor, Object sender) {
        List<CommandExecutor.Executor> executors = Arrays.stream(executor.executors()).toList();
        if (executors.contains(CommandExecutor.Executor.EVERYONE)) return true;
        if (sender instanceof Player && executors.contains(CommandExecutor.Executor.PLAYER)) return true;
        if (sender instanceof ConsoleCommandSender && executors.contains(CommandExecutor.Executor.SERVER)) return true;
        return sender instanceof BlockCommandSender && executors.contains(CommandExecutor.Executor.COMMAND_BLOCK);
    }

    @Override
    public boolean commandExist(String label) {
        if (this.isCommandCached(label))
            return super.commandExist(label);

        if (label.matches(".+:.+"))
            label = label.replace(Iterables.get(Splitter.on(':').split(label), 0) + ":", "");
        label = "/" + label;

        boolean result = Pipe.getBukkit().getPlugin().getServer().getHelpMap().getHelpTopics().stream().map(HelpTopic::getName).toList().contains(label);
        this.cacheCommand(label, result);
        return result;
    }

    @Override
    public void parseCommandResult(CoreCommandImpl command, Object objectSender, CommandResult result) {
        if (!(objectSender instanceof CommandSender sender)) return;
        Locale locale = Pipe.getBukkit().getDefaultLocale();
        if (sender instanceof Player player) locale = Pipe.getBukkit().getClient(player.getUniqueId()).getLocale();

        switch (result) {
            case MISSING_ARGUMENT -> {
                sender.sendMessage(locale.t("pipe.helper.missing_arguments")
                        .arg("label", command.getLabel())
                        .arg("arguments", String.join(", ", this.getMissingArguments(command.getManager()).stream().map(Argument::getKey).toArray(String[]::new))).ct());
            }
            case MISSING_PERMISSION, BAD_EXECUTOR -> {
                sender.sendMessage(locale.t("pipe.helper.permission_denied").arg("label", command.getLabel()).ct());
            }
            case MISSING_EXECUTOR -> {
                Helper helper = new HelperImpl(locale, command.getLabel());
                sender.sendMessage(helper.format(sender).asComponent());
            }
            case COMMAND_NOT_FOUND -> {
                sender.sendMessage(locale.t("pipe.helper.command_not_found").arg("label", command.getLabel()).ct());
            }
            case COMMAND_FAILED -> {
                sender.sendMessage(locale.t("pipe.helper.command_failed").arg("label", command.getLabel()).ct());
            }
            case COMMAND_SUCCESS, NOT_LC_COMMAND -> {}
        }
    }

    /**
     * This function allow MinecraftCommand to be recognized
     * as a global Bukkit Command. This allow users to use
     * tabcompletion in game.
     *
     * @param command the command to register as bukkit command
     * @since 1.0.0
     */
    private void registerCommandAsBukkit(Plugin parent, MinecraftCommand command, BukkitCommand bukkitCommand) {
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
    private void unregisterBukkitCommand(Plugin parent, MinecraftCommand command) {
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

    private void removeCommandLabel(CommandMap commandMap, Map<String, HelpTopic> topics, String label) {
        commandMap.getKnownCommands().remove(label);
        commandMap.getKnownCommands().remove("nymbis:" + label);
        topics.remove("/" + label);
        topics.remove("/nymbis:" + label);
    }

    /**
     * Parse command help to sender if command not executed properly
     *
     * @param command the executed command
     * @param sender  the instance of the command sender
     * @since 1.0.0
     */
    private void parseHelpToSender(Class<?> command, CommandSender sender) {
        sender.sendMessage("(missing HelpUtils)");
    }

}
