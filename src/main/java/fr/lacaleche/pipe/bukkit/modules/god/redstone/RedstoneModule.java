package fr.lacaleche.pipe.bukkit.modules.god.redstone;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;

@AModule(target = ModuleTarget.BUKKIT)
public class RedstoneModule extends Module {

    public RedstoneModule(IModuleHandler handler) {
        super(handler);
    }

}
