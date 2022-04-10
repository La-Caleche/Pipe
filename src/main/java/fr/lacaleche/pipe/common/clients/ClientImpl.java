package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;

import java.util.UUID;

public class ClientImpl extends SqlModel implements Client {

    @Property
    private String uuid;

    @BelongsTo(column = "rank_id")
    private RankImpl rank;

    public ClientImpl(UUID uuid) {
        this.uuid = uuid.toString();
        this.rank = new ModelFilter<RankImpl>().find(RankImpl.class, (rank) -> rank.isDefault());

        this.save();
        this.insert();
    }

    @Override
    public UUID getUUID() {
        return UUID.fromString(this.uuid);
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.rank.getPermissions().contains(permission);
    }

    @Override
    public String toString() {
        return "ClientImpl { uuid='%s', rank='%s' }".formatted(uuid, rank);
    }
}
