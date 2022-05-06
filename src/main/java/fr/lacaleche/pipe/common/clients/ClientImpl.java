package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.util.UUID;

public class ClientImpl extends SqlModel implements Client {

    @Property
    private final String uuid;

    @BelongsTo(column = "rank_id")
    private RankImpl rank;

    @BelongsTo(column = "locale_id")
    private LocaleImpl locale;

    public ClientImpl(UUID uuid) {
        this.uuid = uuid.toString();
        this.rank = new ModelFilter<RankImpl>().find(RankImpl.class, RankImpl::isDefault);
        this.locale = new ModelFilter<LocaleImpl>().find(LocaleImpl.class, LocaleImpl::isDefault);

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
    public void setRank(Rank rank) {
        this.rank = (RankImpl) rank;
        this.save();
    }

    @Override
    public LocaleImpl getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = (LocaleImpl) locale;
        this.save();
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.rank.getPermissions().contains(permission);
    }

    @Override
    public String toString() {
        return "ClientImpl { id='%d', uuid='%s', rank='%s', locale='%s' }".formatted(this.getId(), uuid, rank, locale);
    }
}
