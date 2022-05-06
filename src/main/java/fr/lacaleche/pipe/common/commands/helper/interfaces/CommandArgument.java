package fr.lacaleche.pipe.common.commands.helper.interfaces;

import net.kyori.adventure.text.TextComponent;

public interface CommandArgument {

    SubCommand getCommand();

    String getName();

    boolean isMandatory();

    boolean isMultiple();

    String getType();

    TextComponent.Builder format();

}
