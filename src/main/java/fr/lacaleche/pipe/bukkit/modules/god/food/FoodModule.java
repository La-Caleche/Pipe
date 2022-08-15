package fr.lacaleche.pipe.bukkit.modules.god.food;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;

@AModule(target = ModuleTarget.BUKKIT)
public class FoodModule extends Module {

    public FoodModule(IModuleHandler handler) {
        super(handler);
    }

}
