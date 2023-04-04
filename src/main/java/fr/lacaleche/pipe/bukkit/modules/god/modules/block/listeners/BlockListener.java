package fr.lacaleche.pipe.bukkit.modules.god.modules.block.listeners;

import fr.lacaleche.pipe.bukkit.modules.god.modules.block.BlockModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

public class BlockListener implements Listener {

    private BlockModule module;

    public BlockListener(BlockModule module) {
        this.module = module;
    }

    @EventHandler
    public void onFluidLevelChangeEvent(FluidLevelChangeEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "FLUID_LEVEL_CHANGE", (f, value) -> !value);
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "BLOCK_GROW", (f, value) -> !value);
    }

    @EventHandler
    public void onLeavesDecayEvent(LeavesDecayEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "LEAVES_DECAY", (f, value) -> !value);
    }

    @EventHandler
    public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "BLOCK_PHYSICS", (f, value) -> !value);
    }

    @EventHandler
    public void onBlockDropItemEvent(BlockDropItemEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "BLOCK_DROP_ITEM", (f, value) -> !value);
    }

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "BLOCK_FERTILIZE", (f, value) -> !value);
    }

    @EventHandler
    public void onPlayerHarvestBlockEvent(PlayerHarvestBlockEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "PLAYER_HARVEST_BLOCK", (f, value) -> !value);
    }

    @EventHandler
    public void onEntityBlockChangeEvent(EntityChangeBlockEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "ENTITY_BLOCK_CHANGE", (f, value) -> !value);
    }

    @EventHandler
    public void onWaterFlowEvent(BlockFromToEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "LIQUID_FLOW", (f, value) -> !value);
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
