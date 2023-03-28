package fr.lacaleche.pipe.common.clients.ranks;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;

public class PermissionImpl extends SqlModel implements Permission {

    @Property
    private String slug;

    public PermissionImpl(String slug) {
        super();

        this.slug = slug;

        this.save();
        this.insert();
    }

    @Override
    public String getSlug() {
        return slug;
    }

}
