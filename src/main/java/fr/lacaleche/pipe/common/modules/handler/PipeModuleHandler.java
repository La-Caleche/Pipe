package fr.lacaleche.pipe.common.modules.handler;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.events.interfaces.IListenerManager;
import fr.lacaleche.core.modules.handler.ModuleHandler;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.Pipe;

public class PipeModuleHandler extends ModuleHandler {

    @Override
    public void disableModule(IModule module) {
        super.disableModule(module);

        Pipe.get().getCommandManager().unregisterModuleCommands(module);
        Core.get().<IListenerManager>getListenerManager().unregisterCustomListeners(module);
    }

}
