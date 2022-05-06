package fr.lacaleche.pipe.common.commands.helper.command;

import fr.lacaleche.core.utils.colors.Colors;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.helper.interfaces.CommandArgument;
import fr.lacaleche.pipe.common.commands.helper.interfaces.Helper;
import fr.lacaleche.pipe.common.commands.helper.interfaces.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class SubCommandImpl implements SubCommand {

    private final Helper helper;
    private final String completeCommand;
    private final String name;
    private final List<CommandArgument> arguments;
    private final String description;

    public SubCommandImpl(Helper helper, String fullLabel, String description, Class<?> command) {
        String[] split = fullLabel.split(" ");

        this.helper = helper;
        this.completeCommand = fullLabel;
        this.name = split[split.length - 1];
        this.description = description;
        this.arguments = this.loadArguments(command);
    }

    @Override
    public Helper getHelper() {
        return helper;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<CommandArgument> getArguments() {
        return arguments;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCompleteCommand() {
        return completeCommand;
    }

    @Override
    public TextComponent.Builder format() {
        TextComponent.Builder textBuilder = Component.text();
        textBuilder.append(Component.text("/").append(Component.text(this.getCompleteCommand())).append(Component.text(" ")).color(TextColor.fromHexString(Colors.LC_LIGHT_BLUE)));
        this.getArguments().forEach(argument -> textBuilder.append(argument.format()).append(Component.text(" ")));
        textBuilder.append(Component.text(": ").color(TextColor.fromHexString(Colors.LC_ALT_WHITE_3)));
        textBuilder.append(Component.text(this.getDescription()).color(TextColor.fromHexString(Colors.LC_MAIN_WHITE)));
        return textBuilder;
    }

    private List<CommandArgument> loadArguments(Class<?> command) {
        ArgumentManager manager = Pipe.get().getCommandManager().handleArguments(command);
        List<CommandArgument> arguments = new ArrayList<>();
        manager.getArguments().forEach(argument -> {
            if (argument.getKey().equals("default")) return;
            arguments.add(new CommandArgumentImpl(this, argument.getKey(), argument.isMandatory(), argument.isMultiple(), argument.getClass().getSimpleName().replace("Argument", "")));
        });
        return arguments;
    }

}
