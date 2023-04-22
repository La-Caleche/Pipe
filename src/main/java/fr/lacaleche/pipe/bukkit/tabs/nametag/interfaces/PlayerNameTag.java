package fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.NameTagController;
import fr.lacaleche.pipe.bukkit.tabs.nametag.PlayerNameTagImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public interface PlayerNameTag {

    Map<Integer, Object> getLines();

    Map<TabPlayer, List<NameTagController>> getTabPlayersLines();

    List<NameTagController> getLinesFor(TabPlayer viewer);

    void addLine(Object text, int order);

    void addLineFor(NameTagController line, TabPlayer viewer);

    void removeLine(int order);

    void removeLineFor(TabPlayer viewer, int order);

    boolean hasLine(int order);

    Object getLine(int order);

    Object getFirstLine();

    Object getSecondLine();

    Object getThirdLine();

    Object getLastLine();

    void clearLines();

    void teleport();

    void teleport(TabPlayer viewer);

    void setSneak(boolean sneak);

}
