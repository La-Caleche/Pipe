package fr.lacaleche.pipe.bukkit.modules.god.modules.world;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.features.impl.StoredFeature;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.health.commands.HealCommand;
import fr.lacaleche.pipe.bukkit.modules.god.modules.world.commands.WorldCommand;
import fr.lacaleche.pipe.bukkit.modules.god.modules.world.listeners.WorldListener;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class WorldModule extends BukkitModule {

    public WorldModule(IModuleHandler handler) {
        super(handler);

        this.registerFeatures();
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBoth(this, new WorldListener(this));
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, WorldCommand.class);
    }

    private void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_SKIP", true, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_TYPE", DayLightType.FIXED, DayLightType.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_VALUE", 1600L, Long.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_SPEED_MULTIPLIER", 2, Integer.class));
    }
}
