package fr.lacaleche.pipe.bukkit.modules.god.modules.world.listeners;

import fr.lacaleche.pipe.bukkit.modules.god.modules.world.WorldModule;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.WorldEvent;

public class WorldListener implements Listener {

    private WorldModule module;

    public WorldListener(WorldModule module) {
        this.module = module;
    }

    @EventHandler
    public void onTimeSkipEvent(TimeSkipEvent event) {
        PipeDebug.eventCalled(event);

        this.module.getFeatureManager().<Boolean>cancelConditionnaly(event, "TIME_SKIP", (f, v) -> !v);
    }

}
