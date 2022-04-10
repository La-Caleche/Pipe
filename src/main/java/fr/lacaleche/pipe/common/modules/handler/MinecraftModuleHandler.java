package fr.lacaleche.pipe.common.modules.handler;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.handler.ModuleHandler;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;

public class MinecraftModuleHandler extends ModuleHandler {


    @Override
    public void disableModule(IModule module) {
        super.disableModule(module);

        Pipe.get().getCommandManager().unregisterModuleCommands(module);
        CalecheCore.get().getListenerManager().unregisterCustomListeners(module);
    }

}
