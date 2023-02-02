package fr.lacaleche.pipe.bukkit.modules.god.modules.spawn;

import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class SpawnModule extends BukkitModule {

    public SpawnModule(IModuleHandler handler) {
        super(handler);
    }

}
