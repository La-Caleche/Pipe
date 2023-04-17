package fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.nametag.NameTagController;
import net.kyori.adventure.text.Component;

import java.util.List;

public interface PlayerNameTag {

    List<NameTagController> getLines();

    void addLine(Component text, int order);

    void removeLine(NameTagController line);

    NameTagController getLine(int order);

    NameTagController getFirstLine();

    NameTagController getSecondLine();

    NameTagController getThirdLine();

    NameTagController getLastLine();

    void clearLines();

    void refresh();

}
