package fr.lacaleche.pipe.bukkit.modules.god.spawn;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;

@AModule(target = ModuleTarget.BUKKIT)
public class SpawnModule extends Module {

    public SpawnModule(IModuleHandler handler) {
        super(handler);
    }

}
