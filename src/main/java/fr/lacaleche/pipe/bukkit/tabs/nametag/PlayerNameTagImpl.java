package fr.lacaleche.pipe.bukkit.tabs.nametag;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class PlayerNameTagImpl implements PlayerNameTag {

    private final TabManager tab;
    private final TabPlayer tabPlayer;

    private List<NameTagController> lines;

    public PlayerNameTagImpl(TabManager tab, TabPlayer tabPlayer) {
        this.tab = tab;
        this.tabPlayer = tabPlayer;

        this.lines = new ArrayList<>();
    }

    @Override
    public List<NameTagController> getLines() {
        return this.lines;
    }

    @Override
    public void addLine(Component text, int order) {
        if (text != null) {
            NameTagController line = this.tab.getNmsManager().createEntity(NameTagController.class, this.tabPlayer.getPlayer().getLocation());

            line.setTabPlayer(this.tabPlayer);
            line.setOrder(order);
            line.spawn();
            line.setTitle(text);

            this.lines.add(line);
            this.refresh();
        }
    }

    @Override
    public void removeLine(NameTagController line) {
        if (line != null) {
            this.lines.remove(line);
            this.refresh();
        }
    }

    @Override
    public NameTagController getLine(int order) {
        return this.lines.stream().filter(line -> line.getOrder() == order).findFirst().orElse(null);
    }

    @Override
    public NameTagController getFirstLine() {
        if (this.lines.size() > 0) {
            return this.getLine(0);
        }
        return null;
    }

    @Override
    public NameTagController getSecondLine() {
        if (this.lines.size() > 1) {
            return this.getLine(1);
        }
        return null;
    }

    @Override
    public NameTagController getThirdLine() {
        if (this.lines.size() > 2) {
            return this.getLine(2);
        }
        return null;
    }

    @Override
    public NameTagController getLastLine() {
        return this.getLine(this.lines.size() - 1);
    }

    @Override
    public void clearLines() {
        this.lines.clear();
        this.refresh();
    }

    @Override
    public void refresh() {
        for (NameTagController line : this.lines) {
            line.refresh();
        }
    }
}
