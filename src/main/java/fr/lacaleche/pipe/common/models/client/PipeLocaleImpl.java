package fr.lacaleche.pipe.common.models.client;

import fr.lacaleche.core.models.i18n.LocaleImpl;
import fr.lacaleche.core.models.i18n.interfaces.Translation;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeTranslationBuilder;

public class PipeLocaleImpl extends LocaleImpl<PipeTranslationBuilder> implements PipeLocale {

    @Override
    public PipeTranslationBuilder translate(Translation translation) {
        return new PipeTranslationBuilderImpl(translation, this);
    }

}
