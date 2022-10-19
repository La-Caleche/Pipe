package fr.lacaleche.pipe.bukkit.modules.god.explosion.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.block.BlockModule;
import fr.lacaleche.pipe.bukkit.modules.god.explosion.ExplosionModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionListener implements Listener {

    private ExplosionModule module;

    public ExplosionListener(ExplosionModule module) {
        this.module = module;
    }

    @EventHandler
    public void onBlockExplodeEvent(BlockExplodeEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(ExplosionModule.Features.BLOCK_EXPLODE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_EXPLODE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(ExplosionModule.Features.ENTITY_EXPLODE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ ENTITY_EXPLODE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

}
