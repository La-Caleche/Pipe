package fr.lacaleche.pipe.common.i18n;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.common.i18n.interfaces.TranslationKey;

public class TranslationKeyImpl extends SqlModel implements TranslationKey {

    @Property
    private String translationKey;

    public TranslationKeyImpl(String translationKey) {
        this.translationKey = translationKey;

        this.save();
        this.insert();
    }

    @Override
    public String getKey() {
        return translationKey;
    }

    @Override
    public String toString() {
        return "TranslationKeyImpl { key='%s' }".formatted(translationKey);
    }
}
