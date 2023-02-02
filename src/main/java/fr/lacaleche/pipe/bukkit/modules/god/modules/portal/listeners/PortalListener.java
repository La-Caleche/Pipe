package fr.lacaleche.pipe.bukkit.modules.god.modules.portal.listeners;

import fr.lacaleche.pipe.bukkit.modules.god.modules.portal.PortalModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListener implements Listener {

    private PortalModule module;

    public PortalListener(PortalModule module) {
        this.module = module;
    }

    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "ENTITY_PORTAL", (f, v) -> !v);
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "PLAYER_PORTAL", (f, v) -> !v);
    }

}
