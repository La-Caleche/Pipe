package fr.lacaleche.pipe.common.clients.ranks;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;

import java.util.List;

public class RankImpl extends SqlModel implements Rank {

    @Property
    private String slug;

    @Property
    private String colorCode;

    @Property
    private boolean isDefault;

    @HasMany(type = PermissionImpl.class, table = "rank_permissions", key1 = "rank_id", key2 = "permission_id")
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
