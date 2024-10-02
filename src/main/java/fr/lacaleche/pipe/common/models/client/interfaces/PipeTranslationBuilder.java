package fr.lacaleche.pipe.common.models.client.interfaces;

import fr.lacaleche.core.models.i18n.builder.TranslationBuilder;
import net.kyori.adventure.text.Component;

public interface PipeTranslationBuilder extends TranslationBuilder<PipeTranslationBuilder>  {

    PipeTranslationBuilder arg(String key, Component value);

    Component buildComponent();

}
