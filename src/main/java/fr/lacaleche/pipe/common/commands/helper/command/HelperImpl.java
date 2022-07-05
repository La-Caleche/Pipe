package fr.lacaleche.pipe.common.commands.helper.command;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.helper.interfaces.Helper;
import fr.lacaleche.pipe.common.commands.helper.interfaces.SubCommand;
import fr.lacaleche.pipe.common.commands.utils.CommandsUtils;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelperImpl implements Helper {

    private final Locale locale;
    private final String label;
    private final CoreCommandImpl coreCommand;
    private final List<String> aliases;
    private final List<SubCommand> subCommands;

    public HelperImpl(Locale locale, String label) {
        Class<MinecraftCommand> classCommand = Pipe.get().getCommandManager().getCommand(label);
        MinecraftCommand command = CommandsUtils.validateCommand(classCommand);
        CoreCommandImpl coreCommand = Pipe.get().getCommandManager().handleCommand(null, label, null, null);

        this.locale = locale;
        this.label = label;
        this.coreCommand = coreCommand;
        this.aliases = Arrays.stream(command.aliases()).toList();
        this.subCommands = this.loadSubCommands(label, command.description(), classCommand);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public CoreCommandImpl getCoreCommand() {
        return coreCommand;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public List<SubCommand> getCommands() {
        return subCommands;
    }

    @Override
    public TextComponent.Builder format() {
        TextComponent.Builder textBuilder = Component.text();

        if (this.coreCommand == null) {
            textBuilder.append(this.locale.t("pipe.helper.command_not_found").arg("label", this.label).ct());
            return textBuilder;
        }

        textBuilder.append(this.locale.t("pipe.helper.header").arg("label", this.coreCommand.getLabel()).ct());
        if (this.aliases.size() > 0) {
            textBuilder.append(this.locale.t("pipe.helper.aliases").ct());
            this.aliases.forEach(alias -> textBuilder.append(this.locale.t("pipe.helper.alias").arg("alias", alias).ct()));
        }
        textBuilder.append(Component.newline());
        this.subCommands.forEach(subCommand -> textBuilder.append(subCommand.format()).append(Component.newline()));

        return textBuilder;
    }

    private List<SubCommand> loadSubCommands(String label, String description, Class<?> command) {
        Class<?>[] subCommands = command.getDeclaredClasses();
        if (subCommands.length == 0) return List.of(this.handle(label, description, command));
        return this.handleSubCommand(label, description, command);
    }

    private List<SubCommand> handleSubCommand(String fullLabel, String description, Class<?> commandChild) {
        List<SubCommand> subCommandList = new ArrayList<>();

        Class<?>[] subCommands = commandChild.getDeclaredClasses();
        if (subCommands.length > 0) {
            for (Class<?> subCommand : subCommands) {
                CommandChild child = CommandsUtils.validateChild(subCommand);
                if (child == null) continue;
                List<SubCommand> childList = this.handleSubCommand("%s %s".formatted(fullLabel, child.label()), child.description(), subCommand);
                if (childList.isEmpty()) continue;
                subCommandList.addAll(childList);
            }
        }
        subCommandList.add(this.handle(fullLabel, description, commandChild));
        return subCommandList;
    }

    private SubCommand handle(String fullLabel, String description, Class<?> command) {
        return new SubCommandImpl(this, fullLabel, description, command);
    }

}
