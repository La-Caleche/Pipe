package fr.lacaleche.pipe.common.i18n.builder;

import fr.lacaleche.pipe.common.i18n.interfaces.Translation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class TranslationBuilder {

    private final Translation translation;
    private final List<TArg> arguments;

    public TranslationBuilder(Translation translation) {
        this.translation = translation;
        this.arguments = new ArrayList<>();
    }

    public TranslationBuilder arg(String key, String value) {
        return this.arg(new TArg(key, value));
    }

    public TranslationBuilder arg(TArg argument) {
        this.arguments.add(argument);
        return this;
    }

    public String t() {
        String translation = this.translation.getTranslation();
        for (TArg argument : this.arguments)
            translation = translation.replace("#{" + argument.getKey() + "}", argument.getValue());
        translation = translation.replace("%n%", "\n");
        return translation;
    }

    public Component ct() {
        String text = this.t();
        return MiniMessage.miniMessage().deserialize(text);
    }

}
