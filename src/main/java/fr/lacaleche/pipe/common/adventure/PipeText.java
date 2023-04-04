package fr.lacaleche.pipe.common.adventure;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.CoreImpl;
import fr.lacaleche.pipe.common.adventure.placeholder.PlaceHolderArguments;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface PipeText {

    String serialize(Component component);

    Component deserialize(String text, Locale locale, Map<String, List<?>> placeholders);

    Component deserialize(String text);

    void registerPlaceHolder(String key, BiFunction<PlaceHolderArguments, Locale, Component> placeHolder);

}
