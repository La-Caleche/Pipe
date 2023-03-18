package fr.lacaleche.pipe.common.i18n.builder;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.i18n.interfaces.Translation;
import fr.lacaleche.pipe.common.utils.PipeComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class TranslationBuilderImpl implements TranslationBuilder {

    private final Translation translation;
    private final List<TArg> arguments;
    private String from;
    private Locale locale;

    public TranslationBuilderImpl(Translation translation, Locale locale) {
        this.translation = translation;
        this.arguments = new ArrayList<>();
        this.from = null;
        this.locale = locale;
    }

    @Override
    public TranslationBuilder arg(String key, String value) {
        return this.arg(new TArg(key, value));
    }

    @Override
    public TranslationBuilder arg(String key, int value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, double value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, boolean value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, long value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, float value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, short value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, byte value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, char value) {
        return this.arg(key, String.valueOf(value));
    }

    @Override
    public TranslationBuilder arg(String key, Component value) {
        return this.arg(key, value.toString());
    }

    @Override
    public TranslationBuilder arg(String key, Object value) {
        return this.arg(key, value.toString());
    }

    @Override
    public TranslationBuilder arg(TArg argument) {
        this.arguments.add(argument);
        return this;
    }

    @Override
    public TranslationBuilder from(String from) {
        this.from = from;
        return this;
    }

    @Override
    public String t() {
        String translation = this.translation.getTranslation();
        for (TArg argument : this.arguments) {
            // #{joined_arguments:, :<dark_green>{{argument}}</dark_green>}
            translation = translation.replace("#{" + argument.getKey() + "}", argument.getValue());
        }
        return translation;
    }

    @Override
    public Component ct() {
        String text = this.t();
        if (this.from != null) {
            text = "%s %s".formatted(Core.get().getPrefixFormat().replace("{{from}}", this.from), text);
        }
        Component component = MiniMessage.miniMessage().deserialize(text);

        if (Core.get().inDev() && Core.get().debugEnabled()) {
            Component hoverComponent = Component.text("Key").color(TextColor.fromHexString("#a8d5ff")).append(PipeComponent.separator());

            Component key = Component.text("<none>");
            if (this.translation != null && this.translation.getKey() != null)
                key = Component.text(this.translation.getKey().getKey());
            hoverComponent = hoverComponent.append(key.color(TextColor.fromHexString("#f4f2e5")));

            hoverComponent = hoverComponent.append(Component.newline()).append(Component.text("From").color(TextColor.fromHexString("#a8d5ff"))).append(PipeComponent.separator());

            Component from = Component.text("<nowhere>");
            String stackTrace = CalecheDebug.getFrom();
            if (stackTrace != null)
                from = Component.text(stackTrace);
            hoverComponent = hoverComponent.append(from.color(TextColor.fromHexString("#f4f2e5")));

            component = component.hoverEvent(HoverEvent.showText(hoverComponent));
        }

        return component;
    }

}
