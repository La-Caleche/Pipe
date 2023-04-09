package fr.lacaleche.pipe.bukkit.modules.warps.events;

import fr.lacaleche.core.events.CoreEvent;
import fr.lacaleche.pipe.bukkit.modules.warps.warp.IWarp;

public class WarpDeletedEvent extends CoreEvent {

    private final IWarp warpToBeDeleted;

    public WarpDeletedEvent(IWarp warpToBeDeleted) {
        this.warpToBeDeleted = warpToBeDeleted;
    }

    public IWarp getWarp() {
        return warpToBeDeleted;
    }

}
