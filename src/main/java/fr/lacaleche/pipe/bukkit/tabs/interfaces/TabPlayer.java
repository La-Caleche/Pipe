package fr.lacaleche.pipe.bukkit.tabs.interfaces;

import fr.lacaleche.core.utils.commons.pairs.IPair;
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

    UUID getUniqueId();

    String getName();

    int getGameMode();

    List<IPair<Integer, Component>> getLines();

    Map<String, List<?>> getPlaceHolders();

    void addLine(int line, Component component);

    void removeLine(int line);

    void clearLines();

}
