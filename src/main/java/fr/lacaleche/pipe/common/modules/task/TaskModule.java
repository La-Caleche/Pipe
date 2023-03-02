package fr.lacaleche.pipe.common.modules.task;

import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.modules.task.listeners.UpdateTickListener;

@AModule(target = ModuleTarget.ALL)
public class TaskModule extends Module {

    public TaskModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        Pipe.get().<GlobalListenerManager>getListenerManager().registerCustomListener(this, new UpdateTickListener());
    }

}
