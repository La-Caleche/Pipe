package fr.lacaleche.pipe.common.commands.utils;

import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.SimpleCallback;
import org.joor.Reflect;

public class PipeDebug {

    public static void eventCalled(Object event) {
        String from = CalecheDebug.getFrom();
        Logger.customDebugWCheck("[%s] Event called from : '%s'", event.getClass().getSimpleName(), from);
    }
    
    public static void setCancelled(Object event, boolean cancelled) {
        Reflect reflect = Reflect.on(event);
        String from = CalecheDebug.getFrom();

        try {
            reflect.call("setCancelled", cancelled);
        } catch (Exception e) {
            Logger.err("[%s] Failed to set cancelled from : '%s'. Please be sure that this method is called with a valid event implementing Cancellable", event.getClass().getSimpleName(), from);
        }
        Logger.customDebugWCheck("[%s] Event cancelled set to : '%s' from : '%s'", event.getClass().getSimpleName(), cancelled, from);
    }

    public static void setCancelled(Object event, boolean cancelled, SimpleCallback debugInformations) {
        Reflect reflect = Reflect.on(event);
        String from = CalecheDebug.getFrom();

        try {
            reflect.call("setCancelled", cancelled);
        } catch (Exception e) {
            Logger.err("[%s] Failed to set cancelled from : '%s'. Please be sure that this method is called with a valid event implementing Cancellable", event.getClass().getSimpleName(), from);
        }
        Logger.customDebugWCheck("[%s] Event cancelled set to : '%s' from : '%s'", event.getClass().getSimpleName(), cancelled, from);
        if (debugInformations != null) debugInformations.done();
    }

}
