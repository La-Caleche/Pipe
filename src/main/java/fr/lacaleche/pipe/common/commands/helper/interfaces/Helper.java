package fr.lacaleche.pipe.common.commands.helper.interfaces;

import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public interface Helper {

    CoreCommandImpl getCoreCommand();

    List<String> getAliases();

    List<SubCommand> getCommands();

    TextComponent.Builder format();

    Locale getLocale();

}
