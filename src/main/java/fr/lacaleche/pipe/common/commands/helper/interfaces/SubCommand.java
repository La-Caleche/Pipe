package fr.lacaleche.pipe.common.commands.helper.interfaces;

import net.kyori.adventure.text.TextComponent;

import java.util.List;

public interface SubCommand {

    Helper getHelper();

    List<CommandArgument> getArguments();

    String getCompleteCommand();

    String getName();

    String getDescription();

    TextComponent.Builder format();

}
