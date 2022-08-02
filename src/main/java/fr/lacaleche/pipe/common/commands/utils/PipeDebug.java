package fr.lacaleche.pipe.common.commands.utils;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.core.utils.Logger;
import org.joor.Reflect;

public class PipeDebug {

    public static void eventCalled(Object event) {
        if (!CalecheCore.get().debugEnabled()) return;

        String from = CalecheDebug.getFrom();
        Logger.warn("[%s] Event called from : '%s'".formatted(event.getClass().getSimpleName(), from));
    }
    
    public static void setCancelled(Object event, boolean cancelled) {
        Reflect reflect = Reflect.on(event);
        String from = CalecheDebug.getFrom();
    
        try {
            reflect.set("cancelled", cancelled);
        } catch (Exception e) {
            Logger.err("[%s] Failed to set cancelled from : '%s'. Please be sure that this method is called with a valid event implementing Cancellable".formatted(event.getClass().getSimpleName(), from));
        }
        if (!CalecheCore.get().debugEnabled()) return;
        Logger.warn("[%s] Event cancelled set to : '%s' from : '%s'".formatted(event.getClass().getSimpleName(), cancelled, from));
    }

}
