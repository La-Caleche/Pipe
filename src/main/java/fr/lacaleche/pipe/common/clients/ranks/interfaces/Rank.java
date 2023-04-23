package fr.lacaleche.pipe.common.clients.ranks.interfaces;

import fr.lacaleche.pipe.common.clients.ranks.PermissionImpl;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

@Entity("ranks")
public interface Rank extends ISqlModel {

    String getSlug();

    String getColorCode();

    String getFormattedColor();

    Component colorize(String text);

    boolean isDefault();

    List<PermissionImpl> getPermissions();

    int getPermissionLevel();

    String translatedName(Locale locale);

    Component getColoredRankName(Locale locale);

    TextColor colorAsColor();

    boolean isStaff();

}
