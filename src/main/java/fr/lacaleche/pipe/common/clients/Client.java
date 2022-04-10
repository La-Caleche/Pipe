package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;

import java.util.UUID;

@Entity("clients")
public interface Client extends ISqlModel {

    public UUID getUUID();

    public Rank getRank();

    public boolean hasPermission(Permission permission);

}
