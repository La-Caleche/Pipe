package fr.lacaleche.pipe.common.i18n;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilder;
import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilderImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.i18n.interfaces.Translation;

import java.util.List;

public class LocaleImpl extends SqlModel implements Locale {

    @Property
    private String slug;

    @Property
    private boolean isDefault;

    @HasMany(clazz = TranslationImpl.class, table = "translations", field = "locale", targetField = "id")
    private List<TranslationImpl> translations;

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public List<TranslationImpl> getTranslations() {
        return translations;
    }

    @Override
    public TranslationBuilder translate(String key) {
        TranslationImpl translation = translations.stream().filter(t -> t.getKey().getKey().equals(key)).findFirst().orElse(null);
        if (translation != null) return this.translate(translation);

        return new TranslationBuilderImpl(new TranslationImpl(key));
    }

    @Override
    public TranslationBuilder translate(Translation translation) {
        return new TranslationBuilderImpl(translation);
    }

    @Override
    public TranslationBuilder t(String key) {
        return this.translate(key);
    }

    @Override
    public TranslationBuilder t(Translation translation) {
        return this.translate(translation);
    }

    @Override
    public TranslationBuilder ct(String trueKey, String falseKey, boolean condition) {
        return condition ? this.t(trueKey) : this.t(falseKey);
    }

    @Override
    public TranslationBuilder ct(Translation trueTranslation, Translation falseTranslation, boolean condition) {
        return condition ? this.t(trueTranslation) : this.t(falseTranslation);
    }

    @Override
    public String toString() {
        return "LocaleImpl { slug='%s', default='%b' }".formatted(slug, isDefault);
    }

}
