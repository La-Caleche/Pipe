package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ranks.PermissionImpl;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.ArgumentManagerImpl;
import fr.lacaleche.pipe.common.commands.argument.ArgumentsImpl;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class GlobalCommandManager implements CommandManager {

    private final Map<String, Class<MinecraftCommand>> commands;
    private final Map<String, Class<MinecraftCommand>> aliases;
    private final Map<String, Object> commandsCache;
    private final Map<IModule, List<Class<MinecraftCommand>>> moduleCommands;

    public GlobalCommandManager() {
        this.commands = new HashMap<>();
        this.aliases = new HashMap<>();
        this.commandsCache = new HashMap<>();
        this.moduleCommands = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MinecraftCommand registerNewCommand(IModule module, Class<?> newCommand) {
        MinecraftCommand command = null;
        Class<MinecraftCommand> classCommand = (Class<MinecraftCommand>) newCommand;
        List<Class<MinecraftCommand>> moduleCommandsList = moduleCommands.get(module);

        if (moduleCommandsList == null)
            moduleCommandsList = new ArrayList<>();

        if ((command = CommandsUtils.validateCommand(classCommand)) == null)
            return null;

        if (!this.isRegistered(command.label())) {
            commands.put(command.label(), classCommand);

            moduleCommandsList.add(classCommand);
            moduleCommands.put(module, moduleCommandsList);

            for (String alias : command.aliases()) {
                if (!aliases.containsKey(alias)) {
                    aliases.put(alias, classCommand);
                }
            }
        }
        return command;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean unregisterCommand(IModule module, Class<?> unregistered) {
        MinecraftCommand command = null;
        Class<MinecraftCommand> classCommand = (Class<MinecraftCommand>) unregistered;
        List<Class<MinecraftCommand>> moduleCommandsList = moduleCommands.get(module);

        if (moduleCommandsList == null)
            moduleCommandsList = new ArrayList<>();

        if ((command = CommandsUtils.validateCommand(classCommand)) == null || !this.isRegistered(command.label()))
            return false;

        commands.remove(command.label());

        moduleCommandsList.remove(classCommand);
        moduleCommands.put(module, moduleCommandsList);

        commandsCache.remove(unregistered.getName());

        for (String alias : command.aliases()) {
            aliases.remove(alias);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoreCommandImpl handleCommand(Object sender, String label, String userInput, String[] arguments) {
        if (!isRegistered(label)) return null;

        Class<MinecraftCommand> object = getCommand(label);
        MinecraftCommand coreCommand = CommandsUtils.validateCommand(object);
        if (!coreCommand.enabled()) return null;
        return this.handleChild(label, sender, object, userInput, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoreCommandImpl handleChild(String label, Object sender, Class<?> command, String userInput, String[] arguments) {
        if (arguments != null && arguments.length > 0) {
            for (Class<?> subCommands : command.getDeclaredClasses()) {
                CommandChild child = CommandsUtils.validateChild(subCommands);
                if (child != null && child.label().equalsIgnoreCase(arguments[0]) && child.enabled())
                    return this.handleChild(label, sender, subCommands, userInput, Arrays.copyOfRange(arguments, 1, arguments.length));
            }
        }
        ArgumentManager manager = this.parseArguments(command, arguments);
        CoreCommandImpl coreCommand = new CoreCommandImpl(label, sender, command, manager);
        coreCommand.setUserArguments(arguments);
        coreCommand.setUserInput(userInput);

        return coreCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArgumentManager handleArguments(Class<?> command) {
        ArgumentManager argumentManager = new ArgumentManagerImpl(command);

        Method method = CommandsUtils.validateArguments(command);
        Object instance = Pipe.get().getCommandManager().getCachedInstance(command);

        if (method != null && instance != null) CommandsUtils.invokeArgumentsManager(method, instance, argumentManager);
        argumentManager.addArgument(new StringArgument("default").optional());

        return argumentManager;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public boolean validatePermissions(CommandExecutor commandExecutor, Object sender) {
        CommandExecutor.Executor executor = this.translateSender(sender);
        boolean check = false;

        if (executor == null) return false;
        if (executor == CommandExecutor.Executor.SERVER || executor == CommandExecutor.Executor.COMMAND_BLOCK) return true;

        Client client = this.getClient(sender);
        if (client.getRank().getPermissionLevel() >= 100) return true;

        if (commandExecutor.minPermLevel() == 0 && commandExecutor.strictPermLevel() == 0 && commandExecutor.permissions().length == 0)
            return true;

        if (commandExecutor.minPermLevel() > 0 && client.getRank().getPermissionLevel() >= commandExecutor.minPermLevel())
            check = true;
        else if (commandExecutor.strictPermLevel() > 0 && client.getRank().getPermissionLevel() == commandExecutor.strictPermLevel())
            check = true;

        if (commandExecutor.permissions().length > 0) {
            for (String permName : commandExecutor.permissions()) {
                Permission permission = new ModelFilter<PermissionImpl>().find(PermissionImpl.class, perm -> perm.getSlug().equalsIgnoreCase(permName), sql -> sql.where(new Where("slug", permName)));
                if (client.hasPermission(permission)) check = true;
            }
        }

        return check;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult executeCommand(Object sender, Class<?> command, ArgumentManager manager) {
        Object[] executors = CommandsUtils.validateExecutor(command);
        if (executors == null) return CommandResult.MISSING_EXECUTOR;
        CommandExecutor executor = (CommandExecutor) executors[0];
        Method method = (Method) executors[1];

        Object instance = this.getCachedInstance(command);
        if (instance == null) return CommandResult.COMMAND_NOT_FOUND;
        if (!this.validateSender(executor, sender)) return CommandResult.BAD_EXECUTOR;
        if (!this.validatePermissions(executor, sender)) return CommandResult.MISSING_PERMISSION;

        if (!this.validateArguments(manager)) return CommandResult.MISSING_ARGUMENT;

        return CommandsUtils.invokeExecutor(method, sender, instance, new ArgumentsImpl(manager)) ? CommandResult.COMMAND_SUCESS : CommandResult.COMMAND_FAILED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRegistered(String label) {
        return commands.containsKey(label) || aliases.containsKey(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Class<MinecraftCommand>> getCommands() {
        return commands;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Class<MinecraftCommand>> getAliases() {
        return aliases;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Class<MinecraftCommand>> getModuleCommands(IModule module) {
        return moduleCommands.get(module);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<MinecraftCommand> getCommand(String label) {
        if (!this.isRegistered(label)) return null;
        return commands.containsKey(label) ? commands.get(label) : aliases.get(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parseCompleter(Object sender, Completer completer) {
        ArgumentManager manager = completer.getArgumentManager();
        Argument currentArgument = manager.getAbsoluteArgument(completer.index() >= manager.getArguments().size() ? manager.getArguments().size() - 1 : completer.index());

        for (Class<?> method : manager.getCommand().getDeclaredClasses()) {
            if (method.isAnnotationPresent(CommandChild.class)) {
                CommandChild child = method.getAnnotation(CommandChild.class);
                if (child.label().contains(currentArgument.getValue().toLowerCase()))
                    completer.add(child.label());
            }
        }

        if (completer.next()) completer.incrementIndex();
        int argumentsSize = manager.getArguments().size() - 1;
        if (argumentsSize > 0 && completer.index() <= argumentsSize) {
            Argument argument = manager.getArgument(completer.index());
            argument.completer(completer);

            List<String> completed = new ArrayList<>();
            for (String parsed : completer.getCompleter()) {
                if (!completer.doValidation()) {
                    completed.add(parsed);
                    continue;
                }

                if (completer.next() || parsed.toLowerCase().contains(argument.getValue().toLowerCase()))
                    completed.add(parsed);
            }
            completer.setCompleter(completed);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void customCompleter(Completer completer) {
        Method method = CommandsUtils.validateCompleter(completer.getArgumentManager().getCommand());
        if (method == null) return;

        Object instance = Pipe.get().getCommandManager().getCachedInstance(completer.getArgumentManager().getCommand());
        if (instance == null) return;

        CommandsUtils.invokeCustomCompleter(method, instance, completer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCachedInstance(Class<?> command) {
        Object instance = null;

        if (commandsCache.containsKey(command.getName())) instance = commandsCache.get(command.getName());
        else {
            try {
                commandsCache.put(command.getName(), instance = command.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                SentryAPIImpl.getInstance().captureException(e);
            }
        }
        return instance;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateArguments(ArgumentManager argumentManager) {
        return this.getMissingArguments(argumentManager).isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Argument> getMissingArguments(ArgumentManager argumentManager) {
        Set<Argument> missing = new HashSet<>();
        for (Argument argument : argumentManager.getArguments()) {
            String value = argument.getValue();
            if (argument.isMandatory() && value.isBlank()) missing.add(argument);
        }
        return missing;
    }

    /**
     * Parse simple arguments array to a map with related keys
     * defined in the command annotation header using the field
     * {@link MinecraftCommand} or {@link CommandChild}
     *
     * @return a map containing arguments values related to his keys defined in command annotation
     * @since 1.0.0
     */
    private ArgumentManager parseArguments(Class<?> command, String[] arguments) {
        ArgumentManager manager = this.handleArguments(command);
        if (arguments == null || arguments.length == 0) return manager;

        String join = String.join(" ", arguments);

        manager.getArgument("default").setValue(join);
        if (manager.getArguments().size() == 1) return manager;
        for (int index = 0; index < arguments.length; index++) {
            Argument argument;
            if (index < manager.getArguments().size() - 1) {
                argument = manager.getAbsoluteArgument(index);
                if (argument.getKey().equals("default")) continue;
                argument.setValue(arguments[index]);
            } else {
                argument = manager.getArgument(manager.getArguments().size() - 1);
                if (argument.isMultiple()) argument.setValue(argument.getValue().concat(" " + arguments[index]));
            }
        }
        return manager;
    }
}

