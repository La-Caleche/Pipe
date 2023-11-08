package fr.lacaleche.pipe.common.i18n;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.i18n.interfaces.Translation;

public class TranslationImpl extends SqlModel implements Translation {

    @BelongsTo(column = "translation_key")
    private TranslationKeyImpl translationKey;

    @Property
    private String translation;

    @BelongsTo(column = "locale")
    private LocaleImpl locale;

    public TranslationImpl(LocaleImpl locale, TranslationKeyImpl translationKey, String translation) {
        super();

        this.locale = locale;
        this.translationKey = translationKey;
        this.translation = translation;

        this.insertOrSave();
        this.cache();
    }

    public TranslationImpl(String error) {
        this.translation = error;
    }

    @Override
    public TranslationKeyImpl getKey() {
        return translationKey;
    }

    @Override
    public String getTranslation() {
        return translation;
    }

    @Override
    public LocaleImpl getLocale() {
        return locale;
    }

    @Override
    public String toString() {
        return "TranslationImpl { key='%s', translation='%s', locale='%s' }".formatted(translationKey, translation, locale);
    }
}
