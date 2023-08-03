package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.moderation.BanImpl;
import fr.lacaleche.pipe.common.clients.moderation.KickImpl;
import fr.lacaleche.pipe.common.clients.ranks.PermissionImpl;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class ClientImpl extends SqlModel implements Client {

    @Property
    private final String uuid;
    @Property
    private String username;

    @BelongsTo(column = "rank_id")
    private RankImpl rank;

    @BelongsTo(column = "locale_id")
    private LocaleImpl locale;

    @HasMany(clazz = PermissionImpl.class, table = "client_permissions", field = "client_id", targetField = "permission_id")
    private List<PermissionImpl> permissions;

    @HasMany(clazz = BanImpl.class, field = "client_id")
    private List<BanImpl> bans;

    @HasMany(clazz = KickImpl.class, field = "author_id")
    private List<KickImpl> kicks;

    private List<String> allowedCommands;

    public ClientImpl(UUID uuid, String username) {
        super();

        this.uuid = uuid.toString();
        this.username = username;
        this.rank = new ModelFilter<RankImpl>().model(RankImpl.class).cache(RankImpl::isDefault).getOne();
        this.locale = new ModelFilter<LocaleImpl>().model(LocaleImpl.class).cache(LocaleImpl::isDefault).getOne();
        this.bans = new ArrayList<BanImpl>();
        this.kicks = new ArrayList<KickImpl>();
        this.permissions = new ArrayList<PermissionImpl>();
        this.allowedCommands = new ArrayList<>();

        this.insertOrSave();
        this.cache();
    }

    @Override
    public void loaded() {
        this.allowedCommands = new ArrayList<>();
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
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        this.save();
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
        Permission permission = new ModelFilter<PermissionImpl>().model(PermissionImpl.class).cache(perm -> perm.getSlug().equals(slug)).sql((sqlBuilder) -> sqlBuilder.where(new Where("slug", slug))).getOne();
        return this.hasPermission(permission);
    }

    @Override
    public boolean hasPermissionOrLevel(String permission, int level) {
        if (this.rank.getPermissionLevel() >= level) {
            return true;
        }
        return this.hasPermission(permission);
    }

    @Override
    public boolean hasPermissionOrLevel(Permission permission, int level) {
        if (this.rank.getPermissionLevel() >= level) {
            return true;
        }
        return this.hasPermission(permission);
    }

    @Override
    public List<String> allowedCommands() {
        return this.allowedCommands;
    }

    @Override
    public void addAllowedCommand(String command) {
        this.allowedCommands.add(command);
    }

    @Override
    public BanImpl getLastBan() {
        return this.bans.stream().filter(BanImpl::isActive).findFirst().orElse(null);
    }

    @Override
    public boolean kick(Client author, String reason) {
        if (author == null) {
            SentryAPIImpl.getInstance().captureException(new Exception("Client kick, author is null."));
            return false;
        }

        AtomicBoolean success = new AtomicBoolean(false);

        Pipe.get().getTaskManager().newTask(builder -> builder.run(task -> {
            new KickImpl((ClientImpl) author, this, reason);
            this.refresh();
        }).error(exception -> {
            SentryAPIImpl.getInstance().captureException(exception);
            success.set(false);
        }).async(true).zeroTickExecution(true));

        return success.get();
    }

    @Override
    public boolean ban(Client author, String reason, Date endAt) {
        String end = (endAt == null) ? "Definitive." :  new SimpleDateFormat("dd/MM/yyyy HH:mm").format(endAt);

        if (author == null) {
            SentryAPIImpl.getInstance().captureException(new Exception("Client ban, author is null."));
            return false;
        }

        AtomicBoolean success = new AtomicBoolean(true);

        Pipe.get().getTaskManager().newTask(builder -> builder.run(task -> {
            new BanImpl((ClientImpl) author, this, reason, endAt);
            this.refresh();
        }).error(exception -> {
            SentryAPIImpl.getInstance().captureException(exception);
            success.set(false);
        }).async(true).zeroTickExecution(true));

        return success.get();
    }

    @Override
    public boolean unban(Client author) {
        AtomicBoolean success = new AtomicBoolean(true);

        Pipe.get().getTaskManager().newTask(builder -> builder.run(task -> {
            this.bans.stream().filter(BanImpl::isActive).findFirst().ifPresent(ban -> ban.unban((ClientImpl) author));
        }).error(exception -> {
            SentryAPIImpl.getInstance().captureException(exception);
            success.set(false);
        }).async(true).zeroTickExecution(true));

        return success.get();
    }

    @Override
    public List<BanImpl> getBans() {
        return this.bans;
    }

    @Override
    public List<KickImpl> getKicks() {
        return this.kicks;
    }

    @Override
    public boolean isStaff() {
        return this.getRank().isStaff();
    }

    @Override
    public String toString() {
        return "ClientImpl { id='%d', uuid='%s', rank='%s', locale='%s' }".formatted(this.getId(), uuid, rank, locale);
    }
}
