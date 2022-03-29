package fr.lacaleche.pipe.commands;

import fr.lacaleche.base.clients.Client;
import fr.lacaleche.base.clients.ClientImpl;
import fr.lacaleche.base.commands.GlobalCommandManager;
import fr.lacaleche.base.commands.annotations.CommandExecutor;
import fr.lacaleche.base.commands.annotations.MinecraftCommand;
import fr.lacaleche.base.commands.utils.CommandsUtils;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.utils.PipeCommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandManager extends GlobalCommandManager {

    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        MinecraftCommand command = super.registerNewCommand(module, newCommand);
        PipeCommandUtils.registerCommandAsBukkit(Pipe.get().getPlugin(), command, new SimplePipeCommand(command));
        return command;
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        if(!super.unregisterCommand(module, unregistered)) return false;
        PipeCommandUtils.unregisterBukkitCommand(Pipe.get().getPlugin(), CommandsUtils.validateCommand(unregistered));
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
        return new ModelFilter<ClientImpl>().find(ClientImpl.class, (client) -> client.getUUID().equals(((Player) sender).getUniqueId()));
    }

    @Override
    public boolean validateSender(CommandExecutor executor, Object sender) {
        return PipeCommandUtils.validateSender(executor, (CommandSender) sender);
    }

    @Override
    public void parseHelpToSender(Class<?> command, Object object) {
        PipeCommandUtils.parseHelpToSender(command, (CommandSender) object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNativeCommand(String label) {
        return PipeCommandUtils.isNativeCommand(Pipe.get().getPlugin(), label);
    }

}
