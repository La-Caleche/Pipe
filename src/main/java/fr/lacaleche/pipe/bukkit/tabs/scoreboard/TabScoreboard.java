package fr.lacaleche.pipe.bukkit.tabs.scoreboard;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageMethods.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageConstructor.*;
import fr.lacaleche.pipe.bukkit.tabs.scoreboard.interfaces.Scoreboard;
import net.kyori.adventure.text.Component;

import java.util.*;

public class TabScoreboard implements Scoreboard {

    private final TabManager tab;

    private final TabPlayer tabPlayer;
    private final Set<String> registeredTeams;

    public TabScoreboard(TabManager tab, TabPlayer tabPlayer) {
        this.tab = tab;

        this.tabPlayer = tabPlayer;
        this.registeredTeams = new HashSet<>();
    }

    @Override
    public void registerTeam(String name, Map<String, Object> options) {
        if (!registeredTeams.add(name)) {
            this.updateTeam(name, options);
            return;
        }

        Object emptyScoreboard = this.storage().construct(SCOREBOARD_CONSTRUCTOR);
        Object team = this.storage().construct(SCOREBOARD_TEAM_CONSTRUCTOR, emptyScoreboard, name);

        this.createTeam(team, options);
        Object packet = this.storage().invoke(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM$CREATE_ADD_OR_MODIFY_PACKET, null, team, true);
        this.tab.getNmsManager().sendPacket(this.tabPlayer.getPlayer(), packet);
    }

    @Override
    public void unregisterTeam(String name) {
        if (!registeredTeams.remove(name)) {
            Logger.warn("Tried to unregister non-existing team %s for player %s", name, tabPlayer.getName());
            return;
        }

        Object emptyScoreboard = this.storage().construct(SCOREBOARD_CONSTRUCTOR);
        Object team = this.storage().construct(SCOREBOARD_TEAM_CONSTRUCTOR, emptyScoreboard, name);

        Object packet = this.storage().invoke(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM$CREATE_REMOVE_PACKET, null, team);
        this.tab.getNmsManager().sendPacket(this.tabPlayer.getPlayer(), packet);
    }

    @Override
    public void updateTeam(String name, Map<String, Object> options) {
        if (!registeredTeams.contains(name)) {
            Logger.warn("Tried to modify non-existing team %s for player %s", name, tabPlayer.getName());
            return;
        }

        Object emptyScoreboard = this.storage().construct(SCOREBOARD_CONSTRUCTOR);
        Object team = this.storage().construct(SCOREBOARD_TEAM_CONSTRUCTOR, emptyScoreboard, name);

        this.createTeam(team, options);
        Object packet = this.storage().invoke(PACKET_CLIENTBOUNT_SET_PLAYER_TEAM$CREATE_ADD_OR_MODIFY_PACKET, null, team, true);
        this.tab.getNmsManager().sendPacket(this.tabPlayer.getPlayer(), packet);
    }

    private void createTeam(Object team, Map<String, Object> options) {
        Collection<String> playersNamesSet = this.storage().invoke(SCOREBOARD_TEAMS$GET_PLAYERS, team);
        Set<String> players = (Set<String>) options.get("players");
        playersNamesSet.addAll(players);

        this.storage().invoke(SCOREBOARD_TEAMS$SET_ALLOW_FRIENDLY_FIRE, team, options.get("friendlyFire"));
        this.storage().invoke(SCOREBOARD_TEAMS$SET_CAN_SEE_FRIENDLY_INVISIBILES, team, options.get("friendlyInvisibles"));

        if (options.containsKey("prefix") && options.get("prefix") != null)
            this.storage().invoke(SCOREBOARD_TEAMS$SET_PREFIX, team, options.get("prefix"));
        if (options.containsKey("suffix") && options.get("suffix") != null)
            this.storage().invoke(SCOREBOARD_TEAMS$SET_SUFFIX, team, options.get("suffix"));

        Class<Enum> tagVisibilityClass = (Class<Enum>) this.storage().clazz(TEAM$TAG_VISIBILITY);
        Class<Enum> colisionRuleClass = (Class<Enum>) this.storage().clazz(TEAM$COLLISION_RULE);

        this.storage().invoke(SCOREBOARD_TEAMS$SET_NAME_TAG_VISIBILITY, team, Enum.valueOf(tagVisibilityClass, this.translateEnum((String) options.get("visibility"))));
        this.storage().invoke(SCOREBOARD_TEAMS$SET_COLLISION_RULE, team, Enum.valueOf(colisionRuleClass, this.translateEnum((String) options.get("colision"))));
    }

    private String translateEnum(String option) {
        return option.equalsIgnoreCase("always") ? "ALWAYS" : "NEVER";
    }

    private IStorage storage() {
        return this.tab.getNmsManager().getStorage();
    }

}
