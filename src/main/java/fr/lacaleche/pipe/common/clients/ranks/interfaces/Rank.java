package fr.lacaleche.pipe.common.clients.ranks.interfaces;

import fr.lacaleche.pipe.common.clients.ranks.PermissionImpl;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;

import java.util.List;

@Entity("ranks")
public interface Rank extends ISqlModel {

    public String getSlug();

    public String getColorCode();

    public boolean isDefault();

    public List<PermissionImpl> getPermissions();

    public int getPermissionLevel();

}
