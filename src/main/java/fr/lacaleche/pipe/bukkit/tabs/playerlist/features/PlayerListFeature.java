package fr.lacaleche.pipe.bukkit.tabs.playerlist.features;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.features.AbstractTabFeature;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.*;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist.TabListEntry;
import net.kyori.adventure.text.Component;

import java.util.*;

public class PlayerListFeature extends AbstractTabFeature implements Loadable, Unloadable, Refreshable, JoinListener, DisplayNameListener {

    private boolean disabling = false;

    @Override
    public void load() {
        for (TabPlayer viewer : Pipe.getBukkit().getTabManager().getTabPlayers()) {
            List<TabListEntry> entries = new ArrayList<>();

            for (TabPlayer tabPlayer : Pipe.getBukkit().getTabManager().getTabPlayers()) {
                if (shouldSee(viewer, tabPlayer)) entries.add(this.getTabListInfo(tabPlayer, viewer));
            }
            if (!entries.isEmpty()) viewer.getTabList().addTabListPlayers(entries);
        }
    }

    @Override
    public void join(TabPlayer tabPlayer) {
        List<TabListEntry> entries = new ArrayList<>();
        for (TabPlayer all : Pipe.getBukkit().getTabManager().getTabPlayers()) {
            if (shouldSee(all, tabPlayer)) all.getTabList().addTabListPlayers(Collections.singletonList(this.getTabListInfo(tabPlayer, all)));
            if (shouldSee(tabPlayer, all)) entries.add(this.getTabListInfo(all, tabPlayer));
        }
        if (!entries.isEmpty()) tabPlayer.getTabList().addTabListPlayers(entries);
    }

    @Override
    public Component onDisplayNameChange(TabPlayer tabPlayer, UUID uuid, Component newDisplayName) {
        if (disabling) return newDisplayName;
        TabPlayer packetPlayer = this.tab().getTabPlayer(uuid);
        if (packetPlayer != null) {
            return packetPlayer.getTabList().getTabFormat(tabPlayer.getClient().getLocale());
        }
        return newDisplayName;
    }

    @Override
    public void unload() {
        disabling = true;
        Map<UUID, Component> updatedPlayers = new HashMap<>();
        for (TabPlayer p : this.tab().getTabPlayers()) {
            updatedPlayers.put(p.getUniqueId(), Component.text(p.getPlayer().getName()));
        }
        for (TabPlayer all : this.tab().getTabPlayers()) {
            all.getTabList().updateDisplayNames(updatedPlayers);
        }
    }

    @Override
    public void refresh(TabPlayer tabPlayer) {
        for (TabPlayer viewer : this.tab().getTabPlayers()) {
            viewer.getTabList().updateDisplayNames(Map.of(tabPlayer.getUniqueId(), tabPlayer.getTabList().getTabFormat(viewer.getClient().getLocale())));
        }
    }

    private boolean shouldSee(TabPlayer viewer, TabPlayer tabPlayer) {
        if (viewer == tabPlayer) return true;
        return viewer.getPlayer().canSee(tabPlayer.getPlayer());
    }

}
