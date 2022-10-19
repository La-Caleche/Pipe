package fr.lacaleche.pipe.bukkit.modules.god.portal.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.portal.PortalModule;
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
        if (this.module.isFeatureDisabled(PortalModule.Features.ENTITY_PORTAL)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_PORTAL is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(PortalModule.Features.PLAYER_PORTAL)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_PORTAL is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

}
