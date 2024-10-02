package fr.lacaleche.pipe.common.models.task.listeners;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.events.annotations.CoreEventHandler;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.tasks.events.TickEvent;

public class TickListener implements CoreListener {

    @CoreEventHandler
    public void tickEvent(TickEvent event) {
        final Core core = Core.get();

        if (core.getTaskManager() != null)
            core.getTaskManager().update(event);
    }

}
