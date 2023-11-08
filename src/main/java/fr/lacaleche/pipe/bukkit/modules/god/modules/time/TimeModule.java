package fr.lacaleche.pipe.bukkit.modules.god.modules.time;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.time.commands.TimeCommand;
import fr.lacaleche.pipe.bukkit.modules.god.modules.time.listeners.WorldListener;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class TimeModule extends BukkitModule {

    public TimeModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.getBukkit().getListenerManager();
        bukkitManager.registerBoth(this, new WorldListener(this));
    }

    @Override
    public void registerCommands() {
        Pipe.getBukkit().getCommandManager().registerNewCommand(this, TimeCommand.class);
    }

    @Override
    public void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_SKIP", true, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_TYPE", DayLightType.FIXED, DayLightType.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_VALUE", 1600L, Long.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_SPEED_MULTIPLIER", 2, Integer.class));
    }
}
