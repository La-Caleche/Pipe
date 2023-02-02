package fr.lacaleche.pipe.bukkit.modules.god.modules.block;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.block.listeners.BlockListener;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class BlockModule extends BukkitModule {

    public BlockModule(IModuleHandler handler) {
        super(handler);

        this.registerFeatures();
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new BlockListener(this));
    }

    private void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("FLUID_LEVEL_CHANGE", false));
        this.getFeatureManager().registerFeature(new Feature<>("BLOCK_GROW", false));
        this.getFeatureManager().registerFeature(new Feature<>("LEAVES_DECAY", false));
        this.getFeatureManager().registerFeature(new Feature<>("BLOCK_PHYSICS", false));
        this.getFeatureManager().registerFeature(new Feature<>("BLOCK_DROP_ITEM", false));
        this.getFeatureManager().registerFeature(new Feature<>("BLOCK_FERTILIZE", false));
        this.getFeatureManager().registerFeature(new Feature<>("PLAYER_HARVEST_BLOCK", false));
    }

}
