package fr.lacaleche.pipe.common.adventure;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.common.adventure.placeholder.PlaceHolderArguments;
import fr.lacaleche.pipe.common.adventure.placeholder.PlaceHolderArgumentsImpl;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class PipeTextImpl implements PipeText {

    private final Map<String, BiFunction<PlaceHolderArguments, PipeLocale, Component>> registeredPlaceholders;

    public PipeTextImpl() {
        this.registeredPlaceholders = new HashMap<>();
    }

    @Override
    public String serialize(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    @Override
    public Component deserialize(String text, PipeLocale locale, Map<String, List<?>> placeHoldersValues) {
        Map<String, Component> placeHolders = new HashMap<>();

        this.registeredPlaceholders.keySet().stream().filter(placeHoldersValues::containsKey).forEach(s -> {
            placeHolders.put(s, this.registeredPlaceholders.get(s).apply(new PlaceHolderArgumentsImpl(placeHoldersValues.get(s)), locale));
        });

        return MiniMessage.miniMessage().deserialize(text, placeHolders.keySet().stream().map(key -> Placeholder.component(key, placeHolders.get(key))).toArray(TagResolver[]::new));
    }

    @Override
    public Component deserialize(String text) {
        return this.deserialize(text, (PipeLocale) Core.get().getDefaultLocale(), Map.of());
    }

    @Override
    public void registerPlaceHolder(String key, BiFunction<PlaceHolderArguments, PipeLocale, Component> placeHolder) {
        this.registeredPlaceholders.put(key, placeHolder);
        this.registeredPlaceholders.put("raw_%s".formatted(key), (placeHolderArguments, locale) -> Component.text(((TextComponent) placeHolder.apply(placeHolderArguments, locale)).content()));
    }

}
