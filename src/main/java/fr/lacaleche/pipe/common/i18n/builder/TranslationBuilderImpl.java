package fr.lacaleche.pipe.common.i18n.builder;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.i18n.interfaces.Translation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class TranslationBuilderImpl implements TranslationBuilder {

    private final Translation translation;
    private final List<TArg> arguments;

    public TranslationBuilderImpl(Translation translation) {
        this.translation = translation;
        this.arguments = new ArrayList<>();
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
    public String t() {
        String translation = this.translation.getTranslation();
        for (TArg argument : this.arguments)
            translation = translation.replace("#{" + argument.getKey() + "}", argument.getValue());
        translation = translation.replace("%n%", "\n");
        return translation;
    }

    @Override
    public Component ct() {
        String text = this.t();
        Component component = MiniMessage.miniMessage().deserialize(text);

        if (CalecheCore.get().inDev() && CalecheCore.get().debugEnabled()) {
            Component hoverComponent = Component.text("Key : ").color(NamedTextColor.WHITE);
            if (this.translation == null || this.translation.getKey() == null)
                hoverComponent = hoverComponent.append(Component.text("null"));
            else
                hoverComponent = hoverComponent.append(Component.text(this.translation.getKey().getKey())).color(NamedTextColor.GOLD);

            component = component.hoverEvent(HoverEvent.showText(hoverComponent));
        }

        return component;
    }

}
