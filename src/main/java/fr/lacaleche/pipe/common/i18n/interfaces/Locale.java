package fr.lacaleche.pipe.common.i18n.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;
import fr.lacaleche.pipe.common.i18n.TranslationImpl;
import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilder;
import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilderImpl;

import java.util.List;

@Entity("locales")
public interface Locale extends ISqlModel {

    String getSlug();

    boolean isDefault();

    List<TranslationImpl> getTranslations();

    boolean isTranslated(String key);

    Translation getTranslation(String key);

    TranslationBuilder translate(String key);

    TranslationBuilder translate(Translation translation);

    TranslationBuilder t(String key);

    TranslationBuilder t(Translation translation);

    TranslationBuilder ct(String trueKey, String falseKey, boolean condition);

    TranslationBuilder ct(Translation trueTranslation, Translation falseTranslation, boolean condition);

}
