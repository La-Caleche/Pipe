package fr.lacaleche.pipe.bukkit.modules.god.weather.listeners;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.block.BlockModule;
import fr.lacaleche.pipe.bukkit.modules.god.weather.WeatherModule;
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
        if (this.module.isFeatureDisabled(WeatherModule.Features.LIGHTNING_STRIKE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ LIGHTNING_STRIKE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onThunderChangeEvent(ThunderChangeEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(WeatherModule.Features.THUNDER_CHANGE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ THUNDER_CHANGE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

    @EventHandler
    public void onWeatherChangeEvent(WeatherChangeEvent event) {
        PipeDebug.eventCalled(event);
        if (this.module.isFeatureDisabled(WeatherModule.Features.WEATHER_CHANGE)) {
            PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck("[%s] ↳ WEATHER_CHANGE is disabled".formatted(event.getClass().getSimpleName())));
        }
    }

}
