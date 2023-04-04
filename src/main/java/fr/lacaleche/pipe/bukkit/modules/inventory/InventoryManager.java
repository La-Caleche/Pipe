package fr.lacaleche.pipe.bukkit.modules.inventory;

import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class InventoryManager {

    private Map<Inventory, PipeInventory> inventories;

    public InventoryManager() {
        this.inventories = new HashMap<>();
    }

    public void registerInventory(PipeInventory inventory) {
        this.inventories.put(inventory.getInventory(), inventory);
    }

    public void unregisterInventory(PipeInventory inventory) {
        this.inventories.remove(inventory.getInventory());
    }

    public void closeInventory(Inventory inventory, InventoryCloseEvent.Reason reason) {
        if (reason == InventoryCloseEvent.Reason.UNLOADED) return;
        PipeInventory pipeInventory;
        if ((pipeInventory = this.getInventory(inventory)) == null) return;

        if (reason == InventoryCloseEvent.Reason.PLAYER && !pipeInventory.allowClose()) {
            pipeInventory.show();
            return;
        }

        if (reason == InventoryCloseEvent.Reason.PLAYER) pipeInventory.close();
    }

    public void disable() {
        final Collection<PipeInventory> cache = new ArrayList<>(this.getInventories().values());
        cache.forEach(PipeInventory::close);
        cache.clear();

        this.inventories.clear();
        this.inventories = null;
    }

    public PipeInventory getInventory(Inventory inventory) {
        return getInventories().get(inventory);
    }

    public Map<Inventory, PipeInventory> getInventories() {
        return this.inventories;
    }

}
