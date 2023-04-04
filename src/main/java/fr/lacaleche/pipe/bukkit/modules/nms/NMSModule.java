package fr.lacaleche.pipe.bukkit.modules.nms;

import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;

@AModule(target = ModuleTarget.BUKKIT, disableLocked = true, locked = true, featuresLocked = true)
public class NMSModule extends BukkitModule {

    private NMSManager nmsManager;
    
    public NMSModule(IModuleHandler handler) {
        super(handler);

        this.nmsManager = new NMSManager();
    }

    public NMSManager getNmsManager() {
        return nmsManager;
    }
}
