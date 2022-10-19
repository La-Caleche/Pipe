package fr.lacaleche.pipe.bukkit.modules.god.weather;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.god.block.BlockModule;
import fr.lacaleche.pipe.bukkit.modules.god.block.listeners.BlockListener;
import fr.lacaleche.pipe.bukkit.modules.god.weather.listeners.WeatherListener;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

public class WeatherModule extends Module {

    public WeatherModule(IModuleHandler handler) {
        super(handler);

        Arrays.stream(WeatherModule.Features.values()).forEach(this::registerNewFeature);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new WeatherListener(this));
    }


    public enum Features implements ModuleFeature {
        LIGHTNING_STRIKE,
        THUNDER_CHANGE,
        WEATHER_CHANGE;

        @Override
        public String toString() {
            return super.name();
        }
    }

}
