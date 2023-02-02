package fr.lacaleche.pipe.bukkit.modules.god.modules.explosion.listeners;

import fr.lacaleche.pipe.bukkit.modules.god.modules.explosion.ExplosionModule;
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

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "BLOCK_EXPLODE", (f, value) -> !value);
    }

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "ENTITY_EXPLODE", (f, value) -> !value);
    }

}
