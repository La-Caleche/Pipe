package fr.lacaleche.pipe.bukkit.tabs.interfaces;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.TabFeature;
import fr.lacaleche.pipe.common.clients.Client;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TabManager {

    List<TabPlayer> getTabPlayers();

    TabPlayer getTabPlayer(Client client);

    TabPlayer getTabPlayer(UUID uuid);

    void registerFeature(String featureName, TabFeature feature);

    void unregisterFeature(String featureName);

    void unregisterFeature(TabFeature feature);

    Map<String, TabFeature> getFeatures();

    TabPlayer createPlayer(Player player);

    void loadPlayer(TabPlayer tabPlayer);

    void refreshPlayer(TabPlayer tabPlayer);

    void unloadPlayer(TabPlayer tabPlayer);

    int onGameModeChange(TabPlayer packetReceiver, UUID id, int gameMode);

    int onLatencyChange(TabPlayer packetReceiver, UUID id, int latency);

    Component onDisplayNameChange(TabPlayer packetReceiver, UUID id, Component displayName);

    Object int2GameMode(int gameMode);

    int gameMode2Int(Object gameMode);

    NMSManager getNmsManager();

}
