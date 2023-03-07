package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.ranks.PermissionImpl;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.builder.TranslationBuilder;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.tabs.interfaces.TabManager;
import me.neznamy.tab.api.TabPlayer;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientImpl extends SqlModel implements Client {

    @Property
    private final String uuid;

    @BelongsTo(column = "rank_id")
    private RankImpl rank;

    @BelongsTo(column = "locale_id")
    private LocaleImpl locale;

    @HasMany(clazz = PermissionImpl.class, table = "client_permissions", field = "client_id", targetField = "permission_id")
    private List<PermissionImpl> permissions;

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
        Stream<PermissionImpl> permissions = Stream.concat(
                this.rank.getPermissions().stream(),
                this.permissions.stream()).distinct();
        return permissions.anyMatch(perm -> perm.getId() == permission.getId());
    }

    @Override
    public boolean hasPermission(String slug) {
        Permission permission = new ModelFilter<PermissionImpl>().find(PermissionImpl.class, perm -> perm.getSlug().equals(slug), (sqlBuilder) -> sqlBuilder.where(new Where("slug", slug)));
        return this.hasPermission(permission);
    }

    @Override
    public void loadTab(TabPlayer tabPlayer) {
        TabManager tabManager = Pipe.get().getTabManager();

        this.setTabValue(tabPlayer, "pipe.player.display_name", tabManager::setName);
        this.setTabValue(tabPlayer, "pipe.player.tab_name", tabManager::setTabName);
        this.setTabValue(tabPlayer, "pipe.player.tab_prefix", tabManager::setTabPrefix);
        this.setTabValue(tabPlayer, "pipe.player.tab_suffix", tabManager::setTabSuffix);
    }

    private void setTabValue(TabPlayer tabPlayer, String key, BiConsumer<TabPlayer, Component> consumer) {
        TabManager tabManager = Pipe.get().getTabManager();
        if (!this.locale.isTranslated(key)) return;
        consumer.accept(tabPlayer, this.getLocale().t(key).arg("name", tabPlayer.getName()).arg("rank", this.getRank().translatedName(this.getLocale())).arg("color", this.getRank().getColorCode()).ct());
    }

    @Override
    public String toString() {
        return "ClientImpl { id='%d', uuid='%s', rank='%s', locale='%s' }".formatted(this.getId(), uuid, rank, locale);
    }
}
