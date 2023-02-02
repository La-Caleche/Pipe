package fr.lacaleche.pipe.bukkit.modules;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;

public class BukkitModule extends Module {

    public BukkitModule(IModuleHandler handler) {
        super(handler);

        this.setFeatureManager(new BukkitFeatureManager(this));
    }

}
