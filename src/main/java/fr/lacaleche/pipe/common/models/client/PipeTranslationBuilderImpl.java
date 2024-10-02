package fr.lacaleche.pipe.common.models.client;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.models.i18n.builder.TranslationBuilderImpl;
import fr.lacaleche.core.models.i18n.interfaces.Translation;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeTranslationBuilder;
import fr.lacaleche.pipe.common.utils.PipeComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

public class PipeTranslationBuilderImpl extends TranslationBuilderImpl<PipeTranslationBuilder> implements PipeTranslationBuilder {

    public PipeTranslationBuilderImpl(Translation translation, PipeLocale locale) {
        super(translation, locale);
    }

    @Override
    public PipeTranslationBuilder arg(String key, Component value) {
        return this.arg(key, value.toString());
    }

    @Override
    public Component buildComponent() {
        String text = this.build();
        if (this.from != null) {
            text = "%s %s".formatted(Core.get().conf().getPrefixFormat().replace("{{from}}", this.from), text);
        }
        Component component = Pipe.get().text().deserialize(text, (PipeLocale) locale, this.placeholders);

        if (Core.get().conf().inDev() && Core.get().conf().debugEnabled()) {
            Component hoverComponent = Component.text("Key").color(TextColor.fromHexString("#a8d5ff")).append(PipeComponent.separator());

            Component key = Component.text("<none>");
            if (this.translation != null && this.translation.getKey() != null)
                key = Component.text(this.translation.getKey().getKey());
            hoverComponent = hoverComponent.append(key.color(TextColor.fromHexString("#f4f2e5")));

            hoverComponent = hoverComponent.append(Component.newline()).append(Component.text("From").color(TextColor.fromHexString("#a8d5ff"))).append(PipeComponent.separator());

            String stackTrace = CalecheDebug.getFrom();
            Component from = Component.text(stackTrace);
            hoverComponent = hoverComponent.append(from.color(TextColor.fromHexString("#f4f2e5")));

            component = component.hoverEvent(HoverEvent.showText(hoverComponent));
        }

        return component;
    }

}
