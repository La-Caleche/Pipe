package fr.lacaleche.pipe.bukkit.modules.god.modules.weather.listeners;

import fr.lacaleche.pipe.bukkit.modules.god.modules.weather.WeatherModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {

    private WeatherModule module;

    public WeatherListener(WeatherModule module) {
        this.module = module;
    }

    @EventHandler
    public void onLightningStrikeEvent(LightningStrikeEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "LIGHTNING_STRIKE", (f, v) -> !v);
    }

    @EventHandler
    public void onThunderChangeEvent(ThunderChangeEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "THUNDER_CHANGE", (f, v) -> !v);
    }

    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "WEATHER_CHANGE", (f, v) -> !v);
    }

}
