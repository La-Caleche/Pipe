package fr.lacaleche.pipe.bukkit.tabs.nametag;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerNameTagImpl implements PlayerNameTag {

    private final TabManager tab;
    private final TabPlayer tabPlayer;

    private Map<Integer, Object> lines;
    private Map<TabPlayer, List<NameTagController>> tabPlayersLines;

    public PlayerNameTagImpl(TabManager tab, TabPlayer tabPlayer) {
        this.tab = tab;
        this.tabPlayer = tabPlayer;

        this.lines = new HashMap<>();
        this.tabPlayersLines = new HashMap<>();
    }

    @Override
    public Map<TabPlayer, List<NameTagController>> getTabPlayersLines() {
        return this.tabPlayersLines;
    }

    @Override
    public List<NameTagController> getLinesFor(TabPlayer viewer) {
        return this.tabPlayersLines.get(viewer);
    }

    @Override
    public Map<Integer, Object> getLines() {
        return lines;
    }

    @Override
    public void addLine(Object text, int order) {
        this.lines.put(order, text);
        this.tabPlayersLines.keySet().forEach(viewer -> {
            this.tab.onLineChanged(viewer, this.tabPlayer, order);
        });
    }

    @Override
    public void removeLine(int order) {
        this.lines.remove(order);
        this.tabPlayersLines.values().stream().map(List::stream).flatMap(s -> s).filter(line -> line.getOrder() == order).forEach(NameTagController::setForRemoval);
    }

    @Override
    public void addLineFor(NameTagController line, TabPlayer viewer) {
        if (!this.tabPlayersLines.containsKey(viewer)) {
            this.tabPlayersLines.put(viewer, new ArrayList<>());
        }
        this.tabPlayersLines.get(viewer).add(line);
    }

    @Override
    public void removeLineFor(TabPlayer viewer, int order) {
        this.tabPlayersLines.get(viewer).removeIf(line -> line.getOrder() == order);
    }

    @Override
    public Object getLine(int order) {
        return this.lines.getOrDefault(order, null);
    }

    @Override
    public Object getFirstLine() {
        return this.getLine(0);
    }

    @Override
    public Object getSecondLine() {
        return this.getLine(1);
    }

    @Override
    public Object getThirdLine() {
        return this.getLine(2);
    }


    @Override
    public boolean hasLine(int order) {
        return this.lines.containsKey(order);
    }

    @Override
    public Object getLastLine() {
        int maxOrder = this.lines.keySet().stream().mapToInt(i -> i).max().orElse(-1);
        return this.getLine(maxOrder);
    }

    @Override
    public void clearLines() {
        this.lines.clear();
    }

    @Override
    public void teleport() {
        for (NameTagController line : this.tabPlayersLines.values().stream().flatMap(List::stream).toList()) {
            line.teleport();
        }
    }

    @Override
    public void teleport(TabPlayer viewer) {
        List<NameTagController> controllers = this.tabPlayersLines.computeIfAbsent(viewer, k -> new ArrayList<>());
        for (NameTagController line : controllers) {
            line.teleport();
        }
    }

    @Override
    public void setSneak(boolean sneak) {
        for (NameTagController line : this.tabPlayersLines.values().stream().flatMap(List::stream).toList()) {
            line.setSneak(sneak);
        }
    }
}
