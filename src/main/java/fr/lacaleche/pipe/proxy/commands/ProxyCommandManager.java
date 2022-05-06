package fr.lacaleche.pipe.proxy.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.GlobalCommandManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.proxy.utils.ProxyCommandUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
    public CommandExecutor.Executor translateSender(Object sender) {
        return ProxyCommandUtils.translateSender((CommandSender) sender);
    }

    @Override
    public Client getClient(Object sender) {
        if (!(sender instanceof ProxiedPlayer)) return null;
        return Pipe.get().getClient(((ProxiedPlayer) sender).getUniqueId());
    }

    @Override
    public boolean validateSender(CommandExecutor executor, Object sender) {
        return ProxyCommandUtils.validateSender(executor, (CommandSender) sender);
    }

    @Override
    public void parseHelpToSender(Class<?> command, Object object) {
        ProxyCommandUtils.parseHelpToSender(command, (CommandSender) object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNativeCommand(String label) {
        return ProxyCommandUtils.isNativeCommand(label);
    }

}
