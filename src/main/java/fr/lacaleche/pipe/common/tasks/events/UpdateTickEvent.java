package fr.lacaleche.pipe.common.tasks.events;

import fr.lacaleche.core.events.CoreEvent;

public class UpdateTickEvent extends CoreEvent {

    private final int tick;

    public UpdateTickEvent(int tick) {
        this.tick = tick;
    }

    public int getTick() {
        return this.tick;
    }

}
