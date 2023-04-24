package fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces;

import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.NameTagController;
import fr.lacaleche.pipe.bukkit.tabs.nametag.PlayerNameTagImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public interface PlayerNameTag {

    Map<Integer, IPair<String, Boolean>> getLines();

    Map<TabPlayer, List<NameTagController>> getTabPlayersLines();

    List<NameTagController> getLinesFor(TabPlayer viewer);

    void addLine(String rawText, int order);

    void addLine(Component text, int order);

    void addLineFor(NameTagController line, TabPlayer viewer);

    void removeLine(int order);

    void removeLineFor(TabPlayer viewer, int order);

    void removePlayer(TabPlayer viewer);

    boolean hasLine(int order);

    boolean isPersistent(int order);

    void lockPositionFor(TabPlayer viewer);

    void unlockPositionFor(TabPlayer viewer);

    boolean isPositionLockedFor(TabPlayer viewer);

    IPair<String, Boolean> getLine(int order);

    IPair<String, Boolean> getFirstLine();

    IPair<String, Boolean> getSecondLine();

    IPair<String, Boolean> getThirdLine();

    IPair<String, Boolean> getLastLine();

    void clearLines();

    void teleport();

    void teleport(TabPlayer viewer);

    void setSneak(boolean sneak);

}
