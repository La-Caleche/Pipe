package fr.lacaleche.pipe.common.commands.helper.interfaces;

import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public interface SubCommand {

    Helper getHelper();

    List<CommandArgument> getArguments();

    String getCompleteCommand();

    String getName();

    String getDescription();

    Class<?> getCommand();

    TextComponent.Builder format(Locale locale);

}
