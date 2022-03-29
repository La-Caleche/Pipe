package fr.lacaleche.pipe.modules.command;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.modules.command.listeners.CommandListeners;
import fr.lacaleche.pipe.utils.PipeListenersUtils;

/**
 * Module to manage Minecraft Commands
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 */
public class CommandModule extends Module {
    
    @Override
    public void registerListeners() {
        PipeListenersUtils.registerNewListener(Pipe.get().getPlugin(), new CommandListeners());
    }
    
}
