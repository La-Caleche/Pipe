package fr.lacaleche.pipe.bukkit.modules.hologram;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;

@AModule(target = ModuleTarget.BUKKIT)
public class HologramModule extends BukkitModule {

    private HologramManager hologramManager;

    public HologramModule(IModuleHandler handler) {
        super(handler);
        if (!Core.get().getCentralModuleManager().moduleEnabled(NMSModule.class)) {
            Logger.err("NMS module is not enabled !");
            Pipe.get().shutdown("Please, enable NMS Module before registering this one !");
        }

        this.hologramManager = new HologramManager();
    }

    @Override
    public void onDisable() {
        this.hologramManager.disable();
        this.hologramManager = null;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }
}
