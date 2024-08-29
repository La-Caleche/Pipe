package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.morph.builder.builders.SelectQueryBuilder;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.WhereNot;
import fr.lacaleche.core.databases.mysql.morph.queries.SimpleQuery;
import fr.lacaleche.core.utils.Callback;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.common.clients.moderation.BanImpl;
import fr.lacaleche.pipe.common.clients.moderation.KickImpl;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Entity("clients")
public interface Client extends ISqlModel {

    UUID getUUID();

    Rank getRank();

    String getUsername();

    void setUsername(String username);

    Locale getLocale();

    void setLocale(Locale locale);

    void setRank(Rank rank);

    boolean isStaff();

    boolean isAdmin();

    boolean hasPermission(Permission permission);

    boolean hasPermission(String permission);

    boolean hasPermissionOrLevel(String permission, int level);

    boolean hasPermissionOrLevel(Permission permission, int level);

    List<String> allowedCommands();

    void addAllowedCommand(String command);

    BanImpl getLastBan();

    void kick(Client author, String reason, BiConsumer<Boolean, Optional<Exception>> callback);

    void ban(Client author, String reason, LocalDateTime endAt, BiConsumer<Boolean, Optional<Exception>> callback);

    void unban(Client author, BiConsumer<Boolean, Optional<Exception>> callback);

    List<BanImpl> getBans();

    List<KickImpl> getKicks();

    void startExpiration();

    static Stream<String> allClientUsernames() {
        Collection<String> usernames = new ArrayList<>();
        SimpleQuery<String> query = new SimpleQuery<>(Core.get().<SqlDatabase>getDatabase().getDatastore());
        try (SelectQueryBuilder<String> queryBuilder = new SelectQueryBuilder<>(query).select("username").from("clients").where(new WhereNot.NotNull("username"))) {
            ResultSet set = queryBuilder.execute();
            while (set.next())
                usernames.add(set.getString("username"));
        } catch (SQLException e) {
            SentryAPIImpl.getInstance().captureException(e);
        }
        return usernames.stream();
    }

}
