package fr.lacaleche.pipe.bukkit.commands;

import fr.lacaleche.core.utils.logger.Logger;
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandManager extends GlobalCommandManager {

    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        BukkitPipe pipe = Pipe.getBukkit();

        MinecraftCommand command = super.registerNewCommand(module, newCommand);
        PipeCommandUtils.registerCommandAsBukkit(pipe.getPlugin(), command, new SimpleBukkitPipeCommand(command));
        return command;
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        Logger.customDebug("Unregistering command " + unregistered.getSimpleName() + " from module " + module.getClass().getName());
        BukkitPipe pipe = Pipe.getBukkit();

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
        return Pipe.getBukkit().getClient(((Player) sender).getUniqueId());
    }

    @Override
    public boolean validateSender(CommandExecutor executor, Object sender) {
        return PipeCommandUtils.validateSender(executor, (CommandSender) sender);
    }

    @Override
    public boolean commandExist(String label) {
        return PipeCommandUtils.commandExist(Pipe.getBukkit().getPlugin(), label);
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
                return;
            }
            case MISSING_PERMISSION, BAD_EXECUTOR -> {
                sender.sendMessage(locale.t("pipe.helper.permission_denied").arg("label", command.getLabel()).ct());
                return;
            }
            case MISSING_EXECUTOR -> {
                Helper helper = new HelperImpl(locale, command.getLabel());
                sender.sendMessage(helper.format(sender).asComponent());
                return;
            }
            case COMMAND_NOT_FOUND -> {
                sender.sendMessage(locale.t("pipe.helper.command_not_found").arg("label", command.getLabel()).ct());
                return;
            }
            case COMMAND_FAILED -> {
                sender.sendMessage(locale.t("pipe.helper.command_failed").arg("label", command.getLabel()).ct());
                return;
            }
        }
    }

}
