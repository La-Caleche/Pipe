package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.common.commands.annotations.Permissions;
import fr.lacaleche.pipe.common.commands.interfaces.CloudCommand;
import fr.lacaleche.pipe.common.commands.interfaces.PipeCommandManager;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.setting.ManagerSetting;

import java.util.*;

public class GlobalCommandManager<C> implements PipeCommandManager<C> {

    private CommandManager<C> cloudCommandManager;
    private AnnotationParser<C> cloudAnnotationParser;

    private final Map<IModule, Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>>> moduleCommands;

    public GlobalCommandManager() {
        this.moduleCommands = new HashMap<>();
    }

    @Override
    public void setCloudCommandManager(CommandManager<C> commandManager) {
        this.cloudCommandManager = commandManager;
        this.cloudCommandManager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true);
    }

    @Override
    public void setCloudAnnotationParser(AnnotationParser<C> annotationParser) {
        this.cloudAnnotationParser = annotationParser;
        this.cloudAnnotationParser.registerBuilderModifier(
                Permissions.class,
                (permissions, builder) -> {
                    String stringPermissions = String.join(",", permissions.permissions());
                    String formattedPermission = String.format("perms=%s;strictPermLevel=%d;minPermLevel=%d", stringPermissions, permissions.strictPermLevel(), permissions.minPermLevel());
                    return builder.permission(Permission.of(formattedPermission));
                }
        );
    }

    @Override
    public CommandManager<C> getCloudCommandManager() {
        return this.cloudCommandManager;
    }

    @Override
    public AnnotationParser<C> getCloudAnnotationParser() {
        return this.cloudAnnotationParser;
    }

    @Override
    public void registerNewCommand(IModule module, CloudCommand command) {
        this.requireNonNull();

        final Class<?> commandClass = command.getClass();
        final Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> commands = this.moduleCommands.getOrDefault(module, new HashMap<>());

        if (!isRegisteredFor(module, commandClass)) {
            try {
                commands.put(commandClass, Pair.of(command, this.getCloudAnnotationParser().parse(command)));
            } catch (Exception exception) {
                SentryAPIImpl.getInstance().captureException(exception);
                Logger.err("Their is an error while parsing the command: " + commandClass.getSimpleName());
                return;
            }

            this.moduleCommands.put(module, commands);
        }
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> command) {
        this.requireNonNull();
        if (!isRegisteredFor(module, command)) return false;
        final Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> commands = this.getModuleCommands(module);
        final Pair<CloudCommand, Collection<Command<C>>> commandPair = commands.get(command);

        commandPair.getRight().forEach(cCommand -> this.cloudCommandManager.deleteRootCommand(cCommand.rootComponent().name()));

        commands.remove(command);
        this.moduleCommands.put(module, commands);

        return true;
    }

    @Override
    public void unregisterModuleCommands(IModule module) {
        final Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> commands = this.getModuleCommands(module);
        if (commands == null || commands.isEmpty()) return;

        commands.keySet().forEach(clazz -> this.unregisterCommand(module, clazz));
    }

    @Override
    public Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> getModuleCommands(IModule module) {
        return this.moduleCommands.get(module);
    }

    @Override
    public boolean isRegisteredFor(IModule module, Class<?> commandClazz) {
        final Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> commands = this.moduleCommands.get(module);
        if (commands == null || commands.isEmpty()) return false;

        return commands.keySet().stream().anyMatch(clazz -> clazz.equals(commandClazz));
    }

    @Override
    public Pair<CloudCommand, Collection<Command<C>>> getCommandsFor(IModule module, Class<?> commandClazz) {
        final Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> commands = this.moduleCommands.get(module);
        if (commands == null || commands.isEmpty()) return null;
        return commands.get(commandClazz);
    }

    @Override
    public boolean hasPermission(C sender, Permission permission) {
        return false;
    }

    private void requireNonNull() {
        Objects.requireNonNull(this.cloudCommandManager, "Cloud Command Manager is not set");
        Objects.requireNonNull(this.cloudAnnotationParser, "Cloud Annotation Parser is not set");
    }

}

