package fr.lacaleche.pipe.bukkit.tabs.nametag;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import fr.lacaleche.pipe.bukkit.tabs.nametag.models.PersistentNametag;
import fr.lacaleche.pipe.bukkit.tabs.nametag.models.PersistentNametagImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerNameTagImpl implements PlayerNameTag {

    private final TabManager tab;
    private final TabPlayer tabPlayer;

    private Map<Integer, IPair<String, Boolean>> lines;
    private Map<TabPlayer, List<NameTagController>> tabPlayersLines;
    private Map<TabPlayer, Boolean> positionLocked;

    private List<PersistentNametagImpl> persistentNametags;

    public PlayerNameTagImpl(TabManager tab, TabPlayer tabPlayer) {
        this.tab = tab;
        this.tabPlayer = tabPlayer;

        this.lines = new HashMap<>();
        this.tabPlayersLines = new HashMap<>();
        this.positionLocked = new HashMap<>();

        this.persistentNametags = new ModelFilter<PersistentNametagImpl>().model(PersistentNametagImpl.class)
                .cache(persistentNametag -> persistentNametag.getClient().getId() == this.tabPlayer.getClient().getId())
                .sql(sqlBuilder -> sqlBuilder.where(new Where("client_id", this.tabPlayer.getClient().getId())))
                .getAll().toList();

        if (!this.persistentNametags.isEmpty()) {
            this.persistentNametags.forEach(persistentNametag -> this.lines.put(persistentNametag.getIndexOrder(), new Pair<>(persistentNametag.getRawText(), true)));
        }
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
    public Map<Integer, IPair<String, Boolean>> getLines() {
        return lines;
    }

    @Override
    public boolean isPersistent(int order) {
        return this.lines.getOrDefault(order, new Pair<>(null, false)).getRight();
    }

    @Override
    public void lockPositionFor(TabPlayer viewer) {
        this.positionLocked.put(viewer, true);
    }

    @Override
    public boolean isPositionLockedFor(TabPlayer viewer) {
        return this.positionLocked.getOrDefault(viewer, false);
    }

    @Override
    public void unlockPositionFor(TabPlayer viewer) {
        this.positionLocked.put(viewer, false);
    }

    @Override
    public void addLine(Component text, int order) {
        String rawText = Pipe.getBukkit().text().serialize(text);
        this.addLine(rawText, order);
    }

    @Override
    public void addLine(String rawText, int order) {
        IPair<String, Boolean> line = this.lines.getOrDefault(order, null);

        this.lines.put(order, new Pair<>(rawText, line != null && line.getRight()));
        this.tabPlayersLines.keySet().forEach(viewer -> this.tab.onLineChanged(viewer, this.tabPlayer, order));
    }


    @Override
    public void removeLine(int order) {
        this.lines.remove(order);
        this.tabPlayersLines.values().stream().flatMap(List::stream).filter(line -> line.getOrder() == order).forEach(NameTagController::setForRemoval);
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
    public void removePlayer(TabPlayer viewer) {
        this.tabPlayersLines.remove(viewer);
    }

    @Override
    public IPair<String, Boolean> getLine(int order) {
        return this.lines.getOrDefault(order, null);
    }

    @Override
    public IPair<String, Boolean> getFirstLine() {
        return this.getLine(0);
    }

    @Override
    public IPair<String, Boolean> getSecondLine() {
        return this.getLine(1);
    }

    @Override
    public IPair<String, Boolean> getThirdLine() {
        return this.getLine(2);
    }


    @Override
    public boolean hasLine(int order) {
        return this.lines.containsKey(order);
    }

    @Override
    public IPair<String, Boolean> getLastLine() {
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
