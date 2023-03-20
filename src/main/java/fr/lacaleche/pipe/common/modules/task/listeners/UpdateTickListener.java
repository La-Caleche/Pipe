package fr.lacaleche.pipe.common.modules.task.listeners;

import fr.lacaleche.core.events.annotations.CoreEventHandler;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.tasks.events.UpdateTickEvent;

public class UpdateTickListener implements CoreListener {

    @CoreEventHandler
    public void updateTick(UpdateTickEvent event) {
        Pipe pipe = Pipe.get();

        if (pipe.getTaskManager() != null)
            pipe.getTaskManager().update(event);
    }

}
