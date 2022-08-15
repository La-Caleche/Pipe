package fr.lacaleche.pipe.bukkit.modules.god.worldborder;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;

@AModule(target = ModuleTarget.BUKKIT)
public class WorldBorderModule extends Module {

    public WorldBorderModule(IModuleHandler handler) {
        super(handler);
    }

}
