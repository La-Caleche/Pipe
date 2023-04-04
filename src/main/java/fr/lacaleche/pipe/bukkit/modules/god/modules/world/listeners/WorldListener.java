package fr.lacaleche.pipe.bukkit.modules.god.modules.world.listeners;

import fr.lacaleche.core.events.annotations.CoreEventHandler;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListener;
import fr.lacaleche.pipe.bukkit.modules.god.modules.world.DayLightType;
import fr.lacaleche.pipe.bukkit.modules.god.modules.world.WorldModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import fr.lacaleche.pipe.common.tasks.events.UpdateTickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;

public class WorldListener implements BukkitPipeListener {

    private WorldModule module;

    private static long timeMillis = 0;
    private static long lastTime = 0;

    public WorldListener(WorldModule module) {
        this.module = module;
    }

    @EventHandler
    public void onTimeSkipEvent(TimeSkipEvent event) {
        if (event.getSkipReason() != TimeSkipEvent.SkipReason.CUSTOM) PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "DN_TIME_SKIP", (f, v) -> !v && event.getSkipReason() != TimeSkipEvent.SkipReason.CUSTOM);
    }

    @CoreEventHandler
    public void onTickEvent(UpdateTickEvent event) {
        if (event.getTick() == 0) PipeDebug.eventCalled(event);

        IFeature<DayLightType> type = this.module.getFeatureManager().getFeatureByName("DN_TIME_TYPE");
        IFeature<Long> timeValueFeature = this.module.getFeatureManager().getFeatureByName("DN_TIME_VALUE");
        IFeature<Integer> speedMultiplierFeature = this.module.getFeatureManager().getFeatureByName("DN_SPEED_MULTIPLIER");
        Plugin plugin = Pipe.get().getPlugin();

        switch (type.value().getValue()) {
            case VANILLA_CYCLE:
                break;
            case SPEED_CYCLE:
                plugin.getServer().getWorlds().forEach(w -> w.setTime(w.getTime() +speedMultiplierFeature.value().getValue()));
                break;
            case HALF_CYCLE:
                if (event.getTick() % 2 == 0) {
                    plugin.getServer().getWorlds().forEach(w -> w.setTime(w.getTime() + 1));
                }
                break;
            case FIXED:
            default:
                plugin.getServer().getWorlds().forEach(w -> w.setFullTime(timeValueFeature.value().getValue()));
                break;
        }
    }

}
