package fr.lacaleche.pipe.bukkit.modules.warps.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.pipe.bukkit.modules.inventory.interfaces.PipeInventory;
import fr.lacaleche.pipe.bukkit.modules.warps.warp.IWarp;
import org.bukkit.event.Cancellable;

public class WarpCreatedEvent extends CoreEvent {

    private final IWarp newWarp;

    public WarpCreatedEvent(IWarp newWarp) {
        this.newWarp = newWarp;
    }

    public IWarp getNewWarp() {
        return newWarp;
    }

}
