package fr.lacaleche.pipe.common.clients.ranks;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class RankImpl extends SqlModel implements Rank {

    @Property
    private String slug;

    @Property
    private String colorCode;

    @Property
    private String formattedColor;

    @Property
    private boolean isDefault;

    @HasMany(clazz = PermissionImpl.class, table = "rank_permissions", field = "rank_id", targetField = "permission_id")
    private List<PermissionImpl> permissions;

    @Property
    private int permLevel;

    @Override
    public String getSlug() {
        return slug;
    }

    @Override
    public String getColorCode() {
        return colorCode;
    }

    @Override
    public String getFormattedColor() {
        return formattedColor;
    }

    @Override
    public Component colorize(String text) {
        return Pipe.get().text().deserialize("%s%s".formatted(formattedColor, text));
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public List<PermissionImpl> getPermissions() {
        return permissions;
    }

    @Override
    public int getPermissionLevel() {
        return permLevel;
    }

    @Override
    public String translatedName(Locale locale) {
        return locale.t("global.ranks.%s".formatted(this.slug)).t();
    }

    @Override
    public Component getColoredRankName(Locale locale) {
        return this.colorize(this.translatedName(locale));
    }

    @Override
    public TextColor colorAsColor() {
        return Pipe.get().text().deserialize(formattedColor).color();
    }

    @Override
    public boolean isStaff() {
        return this.getPermissionLevel() > 20;
    }

    @Override
    public String toString() {
        return "RankImpl { slug='%s', colorCode='%s', staff='%b' }".formatted(slug, colorCode, isStaff());
    }
}
