package fr.lacaleche.pipe.common.commands.helper.command;

import fr.lacaleche.pipe.common.commands.helper.interfaces.CommandArgument;
import fr.lacaleche.pipe.common.commands.helper.interfaces.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class CommandArgumentImpl implements CommandArgument {

    private final SubCommand command;
    private final String name;
    private final boolean mandatory;
    private final boolean multiple;
    private final String type;

    public CommandArgumentImpl(SubCommand command, String name, boolean mandatory, boolean multiple, String type) {
        this.command = command;
        this.name = name;
        this.mandatory = mandatory;
        this.multiple = multiple;
        this.type = type;
    }

    @Override
    public SubCommand getCommand() {
        return command;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public TextComponent.Builder format() {
        TextComponent.Builder textBuilder = Component.text();
        TextComponent.Builder hoverBuilder   = Component.text();
        String translationKey = this.multiple ? "command.helper.command.argument.format.multiple" : "command.helper.command.argument.format.classic";
        if (this.mandatory) translationKey = translationKey.concat("_mandatory");

        textBuilder.append(this.command.getHelper().getLocale().t(translationKey).arg("name", this.name).ct());
        hoverBuilder.append(this.command.getHelper().getLocale().t("command.helper.command.argument.type").arg("type", this.type).ct());
        hoverBuilder.append(this.command.getHelper().getLocale().t(this.mandatory ? "command.helper.command.argument.mandatory" : "command.helper.command.argument.not_mandatory").ct());
        hoverBuilder.append(this.command.getHelper().getLocale().t(this.multiple ? "command.helper.command.argument.multiple" : "command.helper.command.argument.not_multiple").ct());

        textBuilder.hoverEvent(hoverBuilder.asComponent().asHoverEvent());

        return textBuilder;
    }
}
