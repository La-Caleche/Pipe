package fr.lacaleche.pipe.bukkit.modules.inventory.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryTrigEvent extends CoreEvent implements Cancellable {

    private PipeInventory inventory;
    private int               index;
    private InventoryClickEvent clickEvent;
    private boolean          cancelled;
    
    public InventoryTrigEvent(PipeInventory inventory, int index, InventoryClickEvent clickEvent) {
        this.inventory = inventory;
        this.index = index;
        this.clickEvent = clickEvent;
    }
    
    public PipeInventory getInventory() {
        return inventory;
    }
    
    public int getIndex() {
        return index;
    }
    
    public InventoryClickEvent getClickEvent() {
        return clickEvent;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
