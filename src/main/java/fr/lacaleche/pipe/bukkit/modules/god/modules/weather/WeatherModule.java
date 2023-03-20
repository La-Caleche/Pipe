package fr.lacaleche.pipe.bukkit.modules.god.modules.weather;

import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.weather.listeners.WeatherListener;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class WeatherModule extends BukkitModule {

    public WeatherModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new WeatherListener(this));
    }

    @Override
    public void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("LIGHTNING_STRIKE", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("THUNDER_CHANGE", false, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("WEATHER_CHANGE", false, Boolean.class));
    }

}
