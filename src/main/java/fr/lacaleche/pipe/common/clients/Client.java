package fr.lacaleche.pipe.common.clients;

import fr.lacaleche.pipe.common.clients.ranks.interfaces.Permission;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import me.neznamy.tab.api.TabPlayer;

import java.util.List;
import java.util.UUID;

@Entity("clients")
public interface Client extends ISqlModel {

    UUID getUUID();

    Rank getRank();

    Locale getLocale();

    void setLocale(Locale locale);

    void setRank(Rank rank);

    boolean hasPermission(Permission permission);

    boolean hasPermission(String permission);

    List<String> allowedCommands();

    void addAllowedCommand(String command);
}
