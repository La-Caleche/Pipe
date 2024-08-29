package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ranks.PermissionImpl;
import fr.lacaleche.pipe.common.commands.annotations.Permissions;
import fr.lacaleche.pipe.common.commands.interfaces.CloudCommand;
import fr.lacaleche.pipe.common.commands.interfaces.PipeCommandManager;
import fr.lacaleche.pipe.common.commands.parsers.*;
import fr.lacaleche.pipe.common.i18n.LocaleCaptionProvider;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.incendo.cloud.CloudCapability;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.caption.CaptionProvider;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.incendo.cloud.setting.ManagerSetting;
import org.joor.Reflect;
import org.joor.ReflectException;

import java.util.*;

public abstract class GlobalCommandManager<C> implements PipeCommandManager<C> {

    private CommandManager<C> cloudCommandManager;
    private AnnotationParser<C> cloudAnnotationParser;
    private LocaleCaptionProvider<C> captionProvider;

    private final Map<IModule, Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>>> moduleCommands;

    public GlobalCommandManager() {
        this.moduleCommands = new HashMap<>();
    }

    @Override
    public void setCloudCommandManager(CommandManager<C> commandManager) {
        this.cloudCommandManager = commandManager;
        this.cloudCommandManager.settings().set(ManagerSetting.ALLOW_UNSAFE_REGISTRATION, true);

        this.cloudCommandManager.parserRegistry()
                .registerNamedParser("client", ClientParser.clientParser())
                .registerNamedParser("cached_client", CachedClientParser.parser())
                .registerNamedParser("joined_string", JoinedStringParser.parser())
                .registerNamedParser("flag_yielding_joined_string", JoinedStringParser.flagYieldingJoinedStringParser())
                .registerParser(DurationDateParser.parser())
                .registerParser(LocaleParser.parser())
                .registerParser(RankParser.parser());

        this.captionProvider = new LocaleCaptionProvider<>();
        this.cloudCommandManager.captionRegistry().registerProvider(this.captionProvider);
    }

    @Override
    public void setCloudAnnotationParser(AnnotationParser<C> annotationParser) {
        this.cloudAnnotationParser = annotationParser;
        this.cloudAnnotationParser.registerBuilderModifier(
                Permissions.class,
                (permissions, builder) -> {
                    String stringPermissions = String.join(",", permissions.permissions());
                    String formattedPermission = String.format("lc_perms:perms=%s;strictPermLevel=%d;minPermLevel=%d", stringPermissions, permissions.strictPermLevel(), permissions.minPermLevel());
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
    public LocaleCaptionProvider<C> getCaptionProvider() {
        return captionProvider;
    }

    @Override
    public void registerNewCommand(IModule module, CloudCommand command) {
        this.requireNonNull();

        final Class<?> commandClass = command.getClass();
        final Map<Class<?>, Pair<CloudCommand, Collection<Command<C>>>> commands = this.moduleCommands.getOrDefault(module, new HashMap<>());

        if (!isRegisteredFor(module, commandClass)) {
            commands.put(commandClass, Pair.of(command, this.getCloudAnnotationParser().parse(command)));
            this.moduleCommands.put(module, commands);
        }
    }

    @Override
    public boolean unregisterCommand(IModule module, Class<?> command) {
        this.requireNonNull();
        if (!this.getCloudCommandManager().hasCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION)) return true;

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

        final Set<Class<?>> commandClasses = Set.copyOf(commands.keySet());
        commandClasses.forEach(clazz -> this.unregisterCommand(module, clazz));
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
    public boolean allowed(Client client, String rootCommand) {
        if (client.isStaff()) return true;

        return this.getCloudCommandManager().rootCommands().stream().anyMatch(s -> {
            if (s.contains(":")) {
                String[] split = s.split(":");
                return split[1].equalsIgnoreCase(rootCommand);
            }
            return s.equalsIgnoreCase(rootCommand);
        });
    }

    @Override
    public PermissionResult hasPermission(Client client, Permission cloudPermission) {
        if (client == null) return PermissionResult.allowed(cloudPermission);
        final Map<String, String> permissionsMap = Arrays.stream(cloudPermission.permissionString().substring(9).split(";")).map(s -> s.split("=")).collect(
                java.util.stream.Collectors.toMap(strings -> strings[0], strings -> strings[1]));
        if (client.isAdmin()) return PermissionResult.allowed(cloudPermission);

        int minPermLevel = getInt(permissionsMap.get("minPermLevel"));
        int strictPermLevel = getInt(permissionsMap.get("strictPermLevel"));
        String[] permissions = permissionsMap.get("perms").split(",");
        boolean allowed = false;

        if (minPermLevel == 0 && strictPermLevel == 0 && permissions.length == 0)
            return PermissionResult.allowed(cloudPermission);

        if (minPermLevel > 0 && client.getRank().getPermissionLevel() >= minPermLevel)
            allowed = true;
        else if (strictPermLevel > 0 && client.getRank().getPermissionLevel() == strictPermLevel)
            allowed = true;

        for (String permName : permissions) {
            fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission permission = new ModelFilter<PermissionImpl>().model(PermissionImpl.class)
                    .cache(perm -> perm.getSlug().equalsIgnoreCase(permName))
                    .sql(sql -> sql.where("slug", permName)).saveInCache()
                    .def(() -> {
                        PermissionImpl newPermission = new PermissionImpl(permName);
                        newPermission.save();
                        return newPermission;
                    })
                    .getOne();
            if (client.hasPermission(permission)) allowed = true;
        }

        return PermissionResult.of(allowed, cloudPermission);
    }

    @Override
    public Locale locale(C sender) {
        return Pipe.get().getLocale(sender);
    }

    private int getInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void requireNonNull() {
        Objects.requireNonNull(this.cloudCommandManager, "Cloud Command Manager is not set");
        Objects.requireNonNull(this.cloudAnnotationParser, "Cloud Annotation Parser is not set");
    }

}

