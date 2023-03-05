package fr.lacaleche.pipe.bukkit.modules.nms;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;

@AModule(target = ModuleTarget.BUKKIT)
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
