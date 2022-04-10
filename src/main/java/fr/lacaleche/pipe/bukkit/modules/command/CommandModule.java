package fr.lacaleche.pipe.bukkit.modules.command;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.command.listeners.CommandListeners;

/**
 * Module to manage Minecraft Commands
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 */
public class CommandModule extends Module {

    public CommandModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        Pipe.get().getListenerManager().registerBukkitListener(this, new CommandListeners());
    }
    
}
