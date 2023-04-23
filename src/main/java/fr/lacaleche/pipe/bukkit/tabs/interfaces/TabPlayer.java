package fr.lacaleche.pipe.bukkit.tabs.interfaces;

import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.interfaces.TabListPlayer;
import fr.lacaleche.pipe.bukkit.tabs.scoreboard.interfaces.Scoreboard;
import fr.lacaleche.pipe.common.clients.Client;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TabPlayer {

    Client getClient();

    Player getPlayer();

    TabListPlayer getTabList();

    Scoreboard getScoreboard();

    PlayerNameTag getNameTag();

    UUID getUniqueId();

    String getName();

    int getGameMode();

    boolean hasMoved();

    void update();

    Map<String, List<?>> getPlaceHolders();

}
