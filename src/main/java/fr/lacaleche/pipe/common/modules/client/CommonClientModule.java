package fr.lacaleche.pipe.common.modules.client;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;

@AModule(target = ModuleTarget.ALL)
public class CommonClientModule extends Module {

    public CommonClientModule(IModuleHandler handler) {
        super(handler);
    }

}
