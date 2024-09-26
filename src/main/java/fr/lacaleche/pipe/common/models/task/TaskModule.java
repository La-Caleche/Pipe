package fr.lacaleche.pipe.common.models.task;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.common.models.task.listeners.TickListener;

@AModule(target = ModuleTarget.ALL)
public class TaskModule extends Module {

    @Override
    public void registerListeners() {
        Core.get().getListenerManager().registerCustomListener(this, new TickListener());
    }

}
