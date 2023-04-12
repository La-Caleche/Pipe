package fr.lacaleche.pipe.bukkit.tabs;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.*;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nms.TabNMSManager;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.world.level.EnumGamemode;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TabManagerImpl implements TabManager {

    private final List<TabPlayer> tabPlayers;
    private final Map<String, TabFeature> features;
    private final TabNMSManager nmsManager;

    public TabManagerImpl() {
        this.tabPlayers = new ArrayList<>();
        this.features = new HashMap<>();
        this.nmsManager = new TabNMSManager();
    }

    @Override
    public Map<String, TabFeature> getFeatures() {
        return features;
    }

    @Override
    public List<TabPlayer> getTabPlayers() {
        return tabPlayers;
    }

    @Override
    public TabPlayer getTabPlayer(UUID uuid) {
        return this.tabPlayers.stream().filter(tabPlayer -> tabPlayer.getPlayer().getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public TabPlayer getTabPlayer(Client client) {
        return this.tabPlayers.stream().filter(tabPlayer -> tabPlayer.getClient().getUUID() == client.getUUID()).findFirst().orElse(null);
    }

    @Override
    public void registerFeature(String featureName, TabFeature feature) {
        this.features.put(featureName, feature);
        if (feature instanceof Loadable loadable)
            loadable.load();
    }

    @Override
    public void unregisterFeature(String featureName) {
        this.unregisterFeature(this.features.get(featureName));
    }

    @Override
    public void unregisterFeature(TabFeature feature) {
        if (feature instanceof Unloadable unloadable)
            unloadable.unload();
        this.features.values().remove(feature);
    }

    @Override
    public TabPlayer createPlayer(Player player) {
        TabPlayer tabPlayer = new TabPlayerImpl(player);
        this.tabPlayers.add(tabPlayer);
        return tabPlayer;
    }

    @Override
    public void loadPlayer(TabPlayer tabPlayer) {
        Pipe.get().getTaskManager().newTask(new TaskBuilder().startAfter(10).callback(task -> this.features.values().forEach(tabFeature -> {
            if (tabFeature instanceof JoinListener joinListener)
                joinListener.join(tabPlayer);
        })));
    }

    @Override
    public void refreshPlayer(TabPlayer tabPlayer) {
        this.features.values().forEach(tabFeature -> {
            if (tabFeature instanceof Refreshable refreshable)
                refreshable.refresh(tabPlayer);
        });
    }

    @Override
    public void unloadPlayer(TabPlayer tabPlayer) {
        this.tabPlayers.remove(tabPlayer);
    }

    @Override
    public int onGameModeChange(TabPlayer packetReceiver, UUID id, int gameMode) {
        AtomicInteger newGameMode = new AtomicInteger(gameMode);
        this.features.values().forEach(tabFeature -> {
            if (tabFeature instanceof GameModeListener gameModeListener)
                newGameMode.set(gameModeListener.onGameModeChange(packetReceiver, id, gameMode));
        });
        return newGameMode.get();
    }

    @Override
    public Component onDisplayNameChange(TabPlayer packetReceiver, UUID id, Component displayName) {
        AtomicReference<Component> newDisplayName = new AtomicReference<>(displayName);
        this.features.values().forEach(tabFeature -> {
            if (tabFeature instanceof DisplayNameListener displayNameListener)
                newDisplayName.set(displayNameListener.onDisplayNameChange(packetReceiver, id, displayName));
        });
        return newDisplayName.get();
    }

    @Override
    public int onLatencyChange(TabPlayer packetReceiver, UUID id, int latency) {
        AtomicInteger newLatency = new AtomicInteger(latency);
        this.features.values().forEach(tabFeature -> {
            if (tabFeature instanceof LatencyListener latencyListener)
                newLatency.set(latencyListener.onLatencyChange(packetReceiver, id, latency));
        });
        return newLatency.get();
    }

    @Override
    public Object int2GameMode(int gameMode) {
        return switch (gameMode) {
            case 1 -> EnumGamemode.b;
            case 2 -> EnumGamemode.c;
            case 3 -> EnumGamemode.d;
            default -> EnumGamemode.a;
        };
    }

    @Override
    public int gameMode2Int(Object gameMode) {
        return switch (String.valueOf(gameMode)) {
            case "CREATIVE" -> 1;
            case "ADVENTURE" -> 2;
            case "SPECTATOR" -> 3;
            default -> 0;
        };
    }

    @Override
    public TabNMSManager getNmsManager() {
        return this.nmsManager;
    }
}
