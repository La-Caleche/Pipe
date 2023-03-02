package fr.lacaleche.pipe.bukkit.modules.inventory;

import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListener;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.inventory.listeners.InventoryListener;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;

@AModule(target = ModuleTarget.BUKKIT)
public class InventoryModule extends BukkitModule {

    private InventoryManager inventoryManager;

    public InventoryModule(IModuleHandler handler) {
        super(handler);

        this.inventoryManager = new InventoryManager();
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager listenerManager = Pipe.get().getListenerManager();
        listenerManager.registerBukkitListener(this, new InventoryListener(this));
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
