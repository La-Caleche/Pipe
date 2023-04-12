package fr.lacaleche.pipe.bukkit.tabs.playerlist.features;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.features.AbstractTabFeature;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.JoinListener;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.Loadable;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.Refreshable;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.common.clients.ranks.interfaces.Rank;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortingFeature extends AbstractTabFeature implements JoinListener, Loadable, Refreshable {

    private boolean disabling = false;
    private Map<TabPlayer, String> cachedKeys = new HashMap<>();

    @Override
    public void join(TabPlayer tabPlayer) {
        for (TabPlayer viewer : Pipe.get().getTabManager().getTabPlayers()) {
            this.constructTeams(viewer);
        }
    }

    @Override
    public void load() {
        for (TabPlayer viewer : Pipe.get().getTabManager().getTabPlayers()) {
            this.constructTeams(viewer);
        }
    }

    @Override
    public void refresh(TabPlayer tabPlayer) {
        String cachedKey = cachedKeys.getOrDefault(tabPlayer, null);
        String newKey = getSortKey(tabPlayer);
        if (cachedKey == null || !cachedKey.equals(newKey)) {
            for (TabPlayer viewer : Pipe.get().getTabManager().getTabPlayers()) {
                if (cachedKey != null) {
                    viewer.getScoreboard().unregisterTeam(cachedKey);
                }
                this.constructTeams(viewer);
            }
        }
    }

    private void constructTeams(TabPlayer viewer) {
        Map<String, Set<TabPlayer>> teams = new HashMap<>();
        for (TabPlayer tabPlayer : Pipe.get().getTabManager().getTabPlayers()) {
            String sortKey = getSortKey(tabPlayer);
            teams.putIfAbsent(sortKey, new HashSet<>());
            teams.get(sortKey).add(tabPlayer);
        }
        teams.forEach((s, tabPlayers) -> viewer.getScoreboard().registerTeam(s, this.constructOptions(s, tabPlayers)));
    }

    private Map<String, Object> constructOptions(String team, Set<TabPlayer> players) {
        Map<String, Object> options = new HashMap<>();
        options.put("players", players.stream().map(TabPlayer::getName).collect(Collectors.toUnmodifiableSet()));
        options.put("friendlyFire", false);
        options.put("friendlyInvisibles", false);
        options.put("visibility", "always");
        options.put("colision", "always");
        return options;
    }

    private String getSortKey(TabPlayer tabPlayer) {
        List<RankImpl> ranks = Core.get().getModelManager().get(RankImpl.class).stream().sorted(Comparator.comparingInt(RankImpl::getPermissionLevel).reversed()).toList();
        Rank playerRank = tabPlayer.getClient().getRank();
        int index = Stream.iterate(0, i -> i + 1).limit(ranks.size()).filter(i -> ranks.toArray()[i].equals(playerRank)).findFirst().orElse(-1);
        String key = "%c".formatted(index + 65);
        cachedKeys.put(tabPlayer, key);
        return key;
    }

}
