package fr.lacaleche.pipe.common.commands.utils;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.common.commands.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utils class used for Minecraft Commands
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 * */
public class CommandsUtils {

    /**
     * Validate a command to check if an executor is
     * registred as method. {@link CommandExecutor}
     *
     * @since 1.0.0
     *
     * @param command the command to check if executor exist
     *
     * @return an array of object, containing the CommandExecutor object, and his annotation class.
     * */
    public static Object[] validateExecutor(Class<?> command) {
        Method methodExecutor = null;
        for (Method declaredMethod : command.getDeclaredMethods()) {
            if (declaredMethod.getName().equalsIgnoreCase("executor")
                    || declaredMethod.getName().equalsIgnoreCase("execute")) {
                methodExecutor = declaredMethod;
                break;
            }
        }
        if (methodExecutor == null || !methodExecutor.isAnnotationPresent(CommandExecutor.class)) return null;
        return new Object[]{methodExecutor.getAnnotation(CommandExecutor.class), methodExecutor};
    }

    /**
     * Call to executor method {@link CommandExecutor}
     *
     * @since 1.0.0
     *
     * @param executor executor method for the command
     * @param sender the instance of the command sender
     *               can be a Player or a CommandSender if
     *               it's a Bukkit Command, or a ProxiedPlayer
     *               if it's a Bungee Command.
     * @param instance instance object of the CoreCommand
     *                 this instance is cached into a Map to
     *                 override memory excess
     * @param arguments arguments
     *
     * @return the result of the method executor
     * */
    public static boolean invokeExecutor(Method executor, Object sender, Object instance, Arguments arguments) {
        try {
            return (boolean) executor.invoke(instance, sender, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            SentryAPIImpl.getInstance().captureException(e);
            SentryAPIImpl.getInstance().captureMessage("Error on command invocation for instance %s with arguments : %s".formatted(instance, "Not implemented yet"));
        }
        return false;
    }

    /**
     * Check if the object is a valid Core Command and return
     * his annotation.
     *
     * @since 1.0.0
     *
     * @param classObject the object to validate
     *
     * @return null if the object has no CoreCommand annotion
     * */
    public static MinecraftCommand validateCommand(Class<?> classObject) {
        if (!isCoreCommand(classObject)) return null;
        return classObject.getAnnotation(MinecraftCommand.class);
    }

    /**
     * Check if the object is a valid Core Child and return
     * his annotation.
     *
     * @since 1.0.0
     *
     * @param classObject the object to validate
     *
     * @return null if the object has no CommandChild annotion
     * */
    public static CommandChild validateChild(Class<?> classObject) {
        if (!isCommandChild(classObject)) return null;
        return classObject.getAnnotation(CommandChild.class);
    }

    /**
     * Check if the object is a valid Core Command
     *
     * @since 1.0.0
     *
     * @param classObject the object to check
     *
     * @return true or false depend if object is a valid Core Command
     * */
    public static boolean isCoreCommand(Class<?> classObject) {
        return classObject.isAnnotationPresent(MinecraftCommand.class);
    }

    /**
     * Check if the object is an Child Command
     *
     * @since 1.0.0
     *
     * @param classObject the object to check
     *
     * @return true or false depend if object is an Child Command
     * */
    public static boolean isCommandChild(Class<?> classObject) {
        return classObject.isAnnotationPresent(CommandChild.class);
    }

    /**
     * TODO
     * */
    public static Method validateArguments(Class<?> command) {
        Method methodExecutor = null;
        for (Method declaredMethod : command.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(ArgumentsManager.class)) {
                methodExecutor = declaredMethod;
                break;
            }
        }
        if (methodExecutor == null) return null;
        return methodExecutor;
    }

    /**
     * Validate a command to check if an tab completer is
     * registred as method. {@link TabCompleter}
     *
     * @since 2.0.0
     *
     * @param command the command to check if executor exist
     *
     * @return an array of object, containing the TabCompleter object, and his annotation class.
     * */
    public static Method validateCompleter(Class<?> command) {
        Method methodExecutor = null;
        for (Method declaredMethod : command.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(TabCompleter.class)) {
                methodExecutor = declaredMethod;
                break;
            }
        }
        if (methodExecutor == null) return null;
        return methodExecutor;
    }

    /**
     * Call to executor method {@link TabCompleter}
     *
     * @since 2.0.0
     *
     * @param tabCompleter tabCompleter method for the command
     * @param instance instance object of the MinecraftCommand
     *                 this instance is cached into a Map to
     *                 override memory excess
     *
     * */
    public static void invokeCustomCompleter(Method tabCompleter, Object instance, Completer completer) {
        try {
            tabCompleter.invoke(instance, completer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            SentryAPIImpl.getInstance().captureException(e);
        }
    }

    /**
     * TODO
     * */
    public static void invokeArgumentsManager(Method method, Object instance, ArgumentManager manager) {
        try {
            method.invoke(instance, manager);
        } catch (IllegalAccessException | InvocationTargetException e) {
            SentryAPIImpl.getInstance().captureException(e);
        }
    }

}
