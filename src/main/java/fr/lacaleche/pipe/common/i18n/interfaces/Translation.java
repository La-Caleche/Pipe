package fr.lacaleche.pipe.common.i18n.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.TranslationKeyImpl;

@Entity("translations")
public interface Translation extends ISqlModel {

    TranslationKeyImpl getKey();

    String getTranslation();

    LocaleImpl getLocale();

}
