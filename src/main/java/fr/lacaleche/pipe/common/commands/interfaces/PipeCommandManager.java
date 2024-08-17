package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.core.modules.interfaces.IModule;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.permission.Permission;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface for Command Manager.
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 * */
public interface PipeCommandManager<C> {

    /**
     * TODO
     * */
    void setCloudCommandManager(CommandManager<C> commandManager);

    /**
     * TODO
     * */
    void setCloudAnnotationParser(AnnotationParser<C> annotationParser);

    /**
     * TODO
     * */
    CommandManager<C> getCloudCommandManager();

    /**
     * TODO
     * */
    AnnotationParser<C> getCloudAnnotationParser();

    /**
     * Function to define a new Minecraft Command.
     *
     * @since 1.0.0
     *
     * @param command object of the newly registred command
     * @return new registered Command
     * */
    void registerNewCommand(IModule module, CloudCommand command);

    /**
     * Function to unregister a valid Core Command.
     *
     * @since 1.0.0
     *
     * @param command class object of the command
     * @return true if command as been succesfully unregistered of false if error occured (Such as command didn't registered yet)
     * */
    boolean unregisterCommand(IModule module, Class<?> command);

    /**
     * TODO
     * */
    void unregisterModuleCommands(IModule module);

    /**
     * TODO
     * */
    Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> getModuleCommands(IModule module);

    /**
     * TODO
     * */
    Pair<CloudCommand, Collection<Command<C>>> getCommandsFor(IModule module, Class<?> commandClazz);

    /**
     * TODO
     * */
    boolean hasPermission(C sender, Permission permission);

    /**
     * TODO
     * */
    boolean isRegisteredFor(IModule module, Class<?> command);

}
