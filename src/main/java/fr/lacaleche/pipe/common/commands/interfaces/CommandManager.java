package fr.lacaleche.pipe.common.commands.interfaces;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.core.modules.interfaces.IModule;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for Command Manager.
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 * */
public interface CommandManager {

    /**
     * Function to define a new Minecraft Command.
     *
     * @since 1.0.0
     *
     * @param newCommand class object of the newly registred command
     * @return new registered Command
     * */
    MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand);

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
    boolean validateSender(CommandExecutor executor, Object sender);

    /**
     * TODO
     * */
    CommandExecutor.Executor translateSender(Object sender);

    /**
     * TODO
     * */
    Client getClient(Object sender);

    /**
     * TODO
     * */
    boolean validatePermissions(CommandExecutor commandExecutor, Object sender);

    /**
     * TODO
     * */
    void parseCommandResult(CoreCommandImpl command, Object sender, CommandResult result);

    /**
     * Function that handle Minecraft Command
     * Triggered when the player execute the command
     *
     * @since 1.0.0
     *
     * @param sender the command sender, can be a CommandSender or a Player
     *               if the command is a BukkitCommand.
     *               ProxiedPlayer if it's a BungeeCommand.
     * @param arguments array of all arguments used for the command
     *
     * @return executeCommand result.
     * */
    CoreCommandImpl handleCommand(Object sender, String label, String userInput, List<String> arguments);

    /**
     * Function that handle Minecraft Command
     * Triggered when the player execute the command
     *
     * @since 1.0.0
     *
     * @param sender the command sender, can be a CommandSender or a Player
     *               if the command is a BukkitCommand.
     *               ProxiedPlayer if it's a BungeeCommand.
     * @param classComand class object of the parent command
     * @param arguments array of all arguments used for the command
     *
     * @return last command occurence.
     * */
    CoreCommandImpl handleChild(String label, Object sender, Class<?> classComand, String userInput, List<String> arguments);

    /**
     * TODO
     * */
    ArgumentManager handleArguments(Class<?> command);

    /**
     * Function that execute the command.
     *
     * @since 1.0.0
     *
     * @param sender the command sender, can be a CommandSender or a Player
     *               if the command is a BukkitCommand.
     *               ProxiedPlayer if it's a BungeeCommand.
     * @param command class object of the currently executed command
     * @param manager argument manager
     *
     * @return true if the command exist even if it's failed. If the command didn't exist, return false.
     * */
    CommandResult executeCommand(Object sender, Class<?> command, ArgumentManager manager);

    /**
     * Check if command is already registred as CoreCommand.
     *
     * @since 1.0.0
     *
     * @param label used to check if command is registred.
     *
     * @return true or false depend if command is registred
     * */
    boolean isRegistered(String label);

    /**
     * Get all commands registred.
     *
     * @since 1.0.0
     *
     * @return Map that contains all registred CoreCommand
     * */
    Map<String, Class<MinecraftCommand>> getCommands();

    /**
     * Get all aliases registred.
     *
     * @since 1.0.0
     *
     * @return Map that contains all registred aliases.
     */
    Map<String, Class<MinecraftCommand>> getAliases();

    /**
     * TODO
     * */
    List<Class<MinecraftCommand>> getModuleCommands(IModule module);

    /**
     * Search a command in the map using it's command label
     *
     * @since 1.0.0
     *
     * @param label command label that will be used to search the
     *              specified command.
     *
     * @return return CoreCommand linked to label if exist
     * */
    Class<MinecraftCommand> getCommand(String label);

    /**
     * Search for cached Fake Command instance from class command
     * or create and cache it if not exist
     *
     * @since 1.0.0
     *
     * @param command the specified command to search
     *
     * @return instance of fake command for the specified command
     * */
    Object getCachedInstance(Class<?> command);

    /**
     * TODO
     * */
    void parseCompleter(Object sender, Completer completer);

    /**
     * TODO
     * */
    void customCompleter(Completer completer);

    /**
     * TODO
     * */
    boolean validateArguments(ArgumentManager manager);

    /**
     * TODO
     * */
    Set<Argument> getMissingArguments(ArgumentManager manager);

    /**
     * TODO
     * */
    boolean commandExist(String label);

}
