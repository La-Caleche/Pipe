package fr.lacaleche.pipe.bukkit.tabs;

import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.interfaces.TabListPlayer;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist.TabListPlayerImpl;
import fr.lacaleche.pipe.common.clients.Client;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.*;

public class TabPlayerImpl implements TabPlayer {

    private final Player player;
    private final TabListPlayer tabListPlayer;

    private final List<IPair<Integer, Component>> lines;

    public TabPlayerImpl(Player player) {
        this.player = player;
        this.tabListPlayer = new TabListPlayerImpl(Pipe.get().getTabManager(), this);

        this.lines = new ArrayList<>();
    }

    @Override
    public Client getClient() {
        return Pipe.get().getClient(this.getUniqueId());
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public TabListPlayer getTabList() {
        return this.tabListPlayer;
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public int getGameMode() {
        return this.player.getGameMode().getValue();
    }

    @Override
    public List<IPair<Integer, Component>> getLines() {
        return lines;
    }

    @Override
    public void addLine(int line, Component component) {
        this.lines.add(new Pair<>(line, component));
    }

    @Override
    public void removeLine(int line) {
        this.lines.removeIf(pair -> pair.getLeft() == line);
    }

    @Override
    public void clearLines() {
        this.lines.clear();
    }

    @Override
    public Map<String, List<?>> getPlaceHolders() {
        Map<String, List<?>> placeholders = new HashMap<>();
        placeholders.put("player", List.of(this.getPlayer()));
        placeholders.put("rank", List.of(this.getPlayer()));
        return placeholders;
    }

}
