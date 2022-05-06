package fr.lacaleche.pipe.common.i18n.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;

@Entity("translation_keys")
public interface TranslationKey extends ISqlModel {

    String getKey();

}
