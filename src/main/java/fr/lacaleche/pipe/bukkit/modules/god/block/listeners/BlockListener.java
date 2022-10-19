package fr.lacaleche.pipe.bukkit.modules.god.block.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.block.BlockModule;
import fr.lacaleche.pipe.bukkit.modules.god.entity.EntityModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class BlockListener implements Listener {

    private BlockModule module;

    public BlockListener(BlockModule module) {
        this.module = module;
    }

    @EventHandler
    public void onFluidLevelChangeEvent(FluidLevelChangeEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.FLUID_LEVEL_CHANGE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ FLUID_LEVEL_CHANGE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.BLOCK_GROW)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_GROW is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.LEAVES_DECAY)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ LEAVES_DECAY is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.BLOCK_PHYSICS)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_PHYSICS is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onBlockDropItemEvent(BlockDropItemEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.BLOCK_DROP_ITEM)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_DROP_ITEM is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.BLOCK_FERTILIZE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ BLOCK_FERTILIZE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onPlayerHarvestBlockEvent(PlayerHarvestBlockEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(BlockModule.Features.PLAYER_HARVEST_BLOCK)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ PLAYER_HARVEST_BLOCK is disabled".formatted(event.getClass().getSimpleName())));
        }
    }








    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        PipeDebug.eventCalled(event);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        PipeDebug.eventCalled(event);
    }

}
