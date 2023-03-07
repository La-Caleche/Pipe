package fr.lacaleche.pipe.proxy.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.pipe.Pipe;
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
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.proxy.utils.ProxyCommandUtils;

import java.util.ArrayList;
import java.util.List;

public class ProxyCommandManager extends GlobalCommandManager {

    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        MinecraftCommand command = super.registerNewCommand(module, newCommand);
        ProxyCommandUtils.registerCommandAsBungee(command, new SimpleProxyPipeCommand(command));
        return command;
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        if(!super.unregisterCommand(module, unregistered)) return false;
        ProxyCommandUtils.unregisterBungeeCommand(CommandsUtils.validateCommand(unregistered));
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
    public CommandExecutor.Executor translateSender(Object source) {
        return ProxyCommandUtils.translateSender((CommandSource) source);
    }

    @Override
    public Client getClient(Object sender) {
        if (!(sender instanceof Player player)) return null;
        return Pipe.get().getClient(player.getUniqueId());
    }

    @Override
    public boolean validateSender(CommandExecutor executor, Object source) {
        return ProxyCommandUtils.validateSender(executor, (CommandSource) source);
    }

    @Override
    public void parseCommandResult(CoreCommandImpl command, Object objectSender, CommandResult result) {
        if (!(objectSender instanceof CommandSource source)) return;
        Locale locale = Pipe.get().getDefaultLocale();
        if (source instanceof Player player) locale = Pipe.get().getClient(player.getUniqueId()).getLocale();

        switch (result) {
            case MISSING_ARGUMENT -> {
                source.sendMessage(locale.t("pipe.helper.missing_arguments")
                        .arg("command", command.getLabel())
                        .arg("arguments", String.join(", ", this.getMissingArguments(command.getManager()).stream().map(Argument::getKey).toArray(String[]::new))).ct());
                return;
            }
        }
    }

}
