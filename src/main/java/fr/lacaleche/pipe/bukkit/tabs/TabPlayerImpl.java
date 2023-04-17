package fr.lacaleche.pipe.bukkit.tabs;

import fr.lacaleche.core.utils.commons.pairs.IPair;
import fr.lacaleche.core.utils.commons.pairs.Pair;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.nametag.PlayerNameTagImpl;
import fr.lacaleche.pipe.bukkit.tabs.nametag.interfaces.PlayerNameTag;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.interfaces.TabListPlayer;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist.TabListPlayerImpl;
import fr.lacaleche.pipe.bukkit.tabs.scoreboard.TabScoreboard;
import fr.lacaleche.pipe.bukkit.tabs.scoreboard.interfaces.Scoreboard;
import fr.lacaleche.pipe.common.clients.Client;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.*;

public class TabPlayerImpl implements TabPlayer {

    private final Player player;
    private final TabListPlayer tabListPlayer;
    private final Scoreboard scoreboard;
    private final PlayerNameTag nameTag;

    public TabPlayerImpl(Player player) {
        this.player = player;
        this.tabListPlayer = new TabListPlayerImpl(Pipe.get().getTabManager(), this);
        this.scoreboard = new TabScoreboard(Pipe.get().getTabManager(), this);
        this.nameTag = new PlayerNameTagImpl(Pipe.get().getTabManager(), this);
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
    public PlayerNameTag getNameTag() {
        return nameTag;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
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
    public Map<String, List<?>> getPlaceHolders() {
        Map<String, List<?>> placeholders = new HashMap<>();
        placeholders.put("player", List.of(this.getPlayer()));
        placeholders.put("rank", List.of(this.getPlayer()));
        return placeholders;
    }

}
