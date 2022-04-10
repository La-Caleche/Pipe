package fr.lacaleche.pipe.common.clients.ranks.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;

@Entity("permissions")
public interface Permission extends ISqlModel {

    public String getSlug();

}
