package fr.lacaleche.pipe.bukkit.modules.inventory.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import org.bukkit.event.Cancellable;

public class InventoryFillEvent extends CoreEvent implements Cancellable {

    private PipeInventory inventory;
    private boolean cancelled;

    public InventoryFillEvent(PipeInventory inventory) {
        this.inventory = inventory;
    }

    public PipeInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
