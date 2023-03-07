package fr.lacaleche.pipe.bukkit.commands;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.colors.AsciiColors;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.GlobalCommandManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.utils.PipeCommandUtils;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitCommandManager extends GlobalCommandManager {

    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        Pipe pipe = Pipe.get();

        MinecraftCommand command = super.registerNewCommand(module, newCommand);
        PipeCommandUtils.registerCommandAsBukkit(pipe.getPlugin(), command, new SimpleBukkitPipeCommand(command));
        return command;
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        Logger.customDebug("Unregistering command " + unregistered.getSimpleName() + " from module " + module.getClass().getName());
        Pipe pipe = Pipe.get();

        if(!super.unregisterCommand(module, unregistered)) return false;
        PipeCommandUtils.unregisterBukkitCommand(pipe.getPlugin(), CommandsUtils.validateCommand(unregistered));
        return true;
    }

    @Override
    public void unregisterModuleCommands(IModule module) {
        List<Class<MinecraftCommand>> commands = this.getModuleCommands(module);
        if (commands == null || commands.size() == 0) return;
        List<Class<MinecraftCommand>> copy = new ArrayList<>(commands);

        for (Class<MinecraftCommand> command : copy) {
            this.unregisterCommand(module, command);
        }
    }

    @Override
    public CommandExecutor.Executor translateSender(Object sender) {
        return PipeCommandUtils.translateSender((CommandSender) sender);
    }

    @Override
    public Client getClient(Object sender) {
        if (!(sender instanceof Player)) return null;
        return Pipe.get().getClient(((Player) sender).getUniqueId());
    }

    @Override
    public boolean validateSender(CommandExecutor executor, Object sender) {
        return PipeCommandUtils.validateSender(executor, (CommandSender) sender);
    }

    @Override
    public void parseCommandResult(CoreCommandImpl command, Object objectSender, CommandResult result) {
        if (!(objectSender instanceof CommandSender sender)) return;
        Locale locale = Pipe.get().getDefaultLocale();
        if (sender instanceof Player player) locale = Pipe.get().getClient(player.getUniqueId()).getLocale();

        switch (result) {
            case MISSING_ARGUMENT -> {
                sender.sendMessage(locale.t("pipe.helper.missing_arguments")
                        .arg("command", command.getLabel())
                        .arg("arguments", String.join(", ", this.getMissingArguments(command.getManager()).stream().map(Argument::getKey).toArray(String[]::new))).ct());
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPluginCommand(String label) {
        return PipeCommandUtils.isPluginCommand(Pipe.get().getPlugin(), label);
    }

}
