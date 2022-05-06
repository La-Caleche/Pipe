package fr.lacaleche.pipe.common.clients.ranks;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class RankImpl extends SqlModel implements Rank {

    @Property
    private String slug;

    @Property
    private String colorCode;

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
    public Component colorize(String text) {
        return MiniMessage.miniMessage().deserialize("%s%s".formatted(colorCode, text));
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
    public String toString() {
        return "RankImpl { slug='%s', colorCode='%s' }".formatted(slug, colorCode);
    }
}
