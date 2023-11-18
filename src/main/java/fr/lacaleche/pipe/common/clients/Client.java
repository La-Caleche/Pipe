package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.pipe.common.clients.moderation.BanImpl;
import fr.lacaleche.pipe.common.clients.moderation.KickImpl;
import fr.lacaleche.pipe.common.clients.moderation.interfaces.IBan;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    boolean hasPermission(Permission permission);

    boolean hasPermission(String permission);

    boolean hasPermissionOrLevel(String permission, int level);

    boolean hasPermissionOrLevel(Permission permission, int level);

    List<String> allowedCommands();

    void addAllowedCommand(String command);

    BanImpl getLastBan();

    boolean kick(Client author, String reason);

    boolean ban(Client author, String reason, LocalDateTime endAt);

    boolean unban(Client author);

    List<BanImpl> getBans();

    List<KickImpl> getKicks();

    void startExpiration();

}
