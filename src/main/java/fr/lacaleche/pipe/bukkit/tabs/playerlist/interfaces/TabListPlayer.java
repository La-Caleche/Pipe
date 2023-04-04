package fr.lacaleche.pipe.bukkit.tabs.playerlist.interfaces;

import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist.TabListEntry;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TabListPlayer {

    TabPlayer getTabPlayer();

    void addTabListPlayers(List<TabListEntry> entries);

    void updateGamemodes(Map<UUID, Integer> entries);

    void updateDisplayNames(Map<UUID, Component> entries);

    Component getTabFormat(Locale locale);

    void setPlayerListFormat(String format);

}
