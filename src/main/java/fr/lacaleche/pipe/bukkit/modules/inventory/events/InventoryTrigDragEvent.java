package fr.lacaleche.pipe.bukkit.modules.inventory.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.Set;

public class InventoryTrigDragEvent extends CoreEvent implements Cancellable {

    private PipeInventory inventory;
    private Set<Integer> slots;
    private InventoryDragEvent dragEvet;
    private boolean cancelled;

    public InventoryTrigDragEvent(PipeInventory inventory, Set<Integer> slots, InventoryDragEvent dragEvet) {
        this.inventory = inventory;
        this.slots = slots;
        this.dragEvet = dragEvet;
    }

    public PipeInventory getInventory() {
        return inventory;
    }

    public Set<Integer> getSlots() {
        return slots;
    }

    public InventoryDragEvent getDragEvent() {
        return dragEvet;
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
