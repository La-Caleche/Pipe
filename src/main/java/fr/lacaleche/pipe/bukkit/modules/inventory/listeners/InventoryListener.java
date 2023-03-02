package fr.lacaleche.pipe.bukkit.modules.inventory.listeners;

import fr.lacaleche.pipe.bukkit.modules.inventory.InventoryModule;
import fr.lacaleche.pipe.bukkit.modules.inventory.events.InventoryTrigEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    private InventoryModule inventoryModule;

    public InventoryListener(InventoryModule inventoryModule) {
        this.inventoryModule = inventoryModule;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        PipeInventory inventory = this.inventoryModule.getInventoryManager().getInventory(event.getInventory());

        if (inventory != null && inventory.isVisible()) {
            InventoryTrigEvent trigEvent = new InventoryTrigEvent(inventory, event.getSlot(), event);
            trigEvent.call();

            if (!trigEvent.isCancelled())
                inventory.trigItem(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        this.inventoryModule.getInventoryManager().closeInventory(event.getInventory(), event.getReason());
    }

}
