package fr.lacaleche.pipe.bukkit.modules.handler;

import fr.lacaleche.pipe.common.modules.handler.MinecraftModuleHandler;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;

public class BukkitPipeModuleHandler extends MinecraftModuleHandler {

    @Override
    public void disableModule(IModule module) {
        super.disableModule(module);

        if (Core.get().getListenerManager() instanceof BukkitPipeListenerManager manager) {
            manager.unregisterBukkitListeners(module);
        }
    }
    
}
