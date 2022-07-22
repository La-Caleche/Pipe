package fr.lacaleche.pipe.common.commands.utils;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.core.utils.Logger;

public class PipeDebug {

    public static void eventCalled(Object event) {
        if (!CalecheCore.get().debugEnabled()) return;

        String from = CalecheDebug.getFrom();
        Logger.warn("[%s] Event called from : '%s'".formatted(event.getClass().getSimpleName(), from));
    }

}
