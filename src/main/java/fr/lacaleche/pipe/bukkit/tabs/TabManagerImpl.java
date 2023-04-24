package fr.lacaleche.pipe.bukkit.tabs;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.*;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.nms.TabNMSManager;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.world.level.EnumGamemode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TabManagerImpl implements TabManager {

    private final List<TabPlayer> tabPlayers;
    private final Map<String, TabFeature> features;
    private final TabNMSManager nmsManager;

    private final Map<Module, List<TriConsumer<TabPlayer, Player, Client>>> playerLoadCallbacks;
    private final Map<Module, List<TriConsumer<TabPlayer, Player, Client>>> playerUnloadCallbacks;

    public TabManagerImpl() {
        this.tabPlayers = new ArrayList<>();
        this.features = new HashMap<>();
        this.nmsManager = new TabNMSManager();

        this.playerLoadCallbacks = new HashMap<>();
        this.playerUnloadCallbacks = new HashMap<>();
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
    public TabPlayer getTabPlayer(Player player) {
        return this.getTabPlayer(player.getUniqueId());
    }

    @Override
    public TabPlayer getTabPlayer(Client client) {
        return this.tabPlayers.stream().filter(tabPlayer -> tabPlayer.getClient().getUUID() == client.getUUID()).findFirst().orElse(null);
    }

    @Override
    public void registerFeature(String featureName, TabFeature feature) {
        this.features.put(featureName, feature);
    }

    @Override
    public void loadFeatures() {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof Loadable loadable)
                loadable.load();
        }
    }

    @Override
    public void writePacket(TabPlayer tabPlayer, Object packet) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof PacketWriteListener packetWriteListener)
                packetWriteListener.writePacket(tabPlayer, packet);
        }
    }

    @Override
    public void entityMove(TabPlayer viewer, TabPlayer player, boolean force) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof EntityMoveListener entityMoveListener)
                entityMoveListener.onEntityMove(viewer, player, force);
        }
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
        Pipe.getBukkit().getTaskManager().newTask(new TaskBuilder().startAfter(10).callback(task -> {
            this.getPlayerLoadCallbacks().values().stream()
                    .flatMap(Collection::stream)
                    .forEach(callback -> callback.accept(tabPlayer, tabPlayer.getPlayer(), tabPlayer.getClient()));

            for (TabFeature tabFeature : this.features.values()) {
                if (tabFeature instanceof JoinListener joinListener)
                    joinListener.join(tabPlayer);
            }
        }));
    }

    @Override
    public void refreshPlayer(TabPlayer tabPlayer) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof Refreshable refreshable)
                refreshable.refresh(tabPlayer);
        }
    }

    @Override
    public void unloadPlayer(TabPlayer tabPlayer) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof QuitListener quitListener)
                quitListener.quit(tabPlayer);
        }

        this.getPlayerUnloadCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(tabPlayer, tabPlayer.getPlayer(), tabPlayer.getClient()));

        this.tabPlayers.remove(tabPlayer);
    }

    @Override
    public void onPlayerSneak(TabPlayer tabPlayer, boolean sneak) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof PlayerSneakListener sneakListener)
                sneakListener.onPlayerSneak(tabPlayer, sneak);
        }
    }

    @Override
    public int onGameModeChange(TabPlayer packetReceiver, UUID id, int gameMode) {
        AtomicInteger newGameMode = new AtomicInteger(gameMode);
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof GameModeListener gameModeListener)
                newGameMode.set(gameModeListener.onGameModeChange(packetReceiver, id, gameMode));
        }
        return newGameMode.get();
    }

    @Override
    public void onWorldChange(TabPlayer player, String world) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof WorldChangeListener worldChangeListener)
                worldChangeListener.onWorldChange(player, world);
        }
    }

    @Override
    public void onLineChanged(TabPlayer viewer, TabPlayer player, int line) {
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof NameTagLineChangedListener lineChangeListener)
                lineChangeListener.onLineChanged(viewer, player, line);
        }
    }

    @Override
    public Component onDisplayNameChange(TabPlayer packetReceiver, UUID id, Component displayName) {
        AtomicReference<Component> newDisplayName = new AtomicReference<>(displayName);
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof DisplayNameListener displayNameListener)
                newDisplayName.set(displayNameListener.onDisplayNameChange(packetReceiver, id, displayName));
        }
        return newDisplayName.get();
    }

    @Override
    public int onLatencyChange(TabPlayer packetReceiver, UUID id, int latency) {
        AtomicInteger newLatency = new AtomicInteger(latency);
        for (TabFeature tabFeature : this.features.values()) {
            if (tabFeature instanceof LatencyListener latencyListener)
                newLatency.set(latencyListener.onLatencyChange(packetReceiver, id, latency));
        }
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
    public Map<Module, List<TriConsumer<TabPlayer, Player, Client>>> getPlayerLoadCallbacks() {
        return playerLoadCallbacks;
    }

    @Override
    public Map<Module, List<TriConsumer<TabPlayer, Player, Client>>> getPlayerUnloadCallbacks() {
        return playerUnloadCallbacks;
    }

    @Override
    public void addPlayerLoadCallback(Module module, TriConsumer<TabPlayer, Player, Client> callback) {
        List<TriConsumer<TabPlayer, Player, Client>> moduleCallbacks = this.playerLoadCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.playerLoadCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public void addPlayerUnloadCallback(Module module, TriConsumer<TabPlayer, Player, Client> callback) {
        List<TriConsumer<TabPlayer, Player, Client>> moduleCallbacks = this.playerUnloadCallbacks.getOrDefault(module, new ArrayList<>());
        moduleCallbacks.add(callback);
        this.playerUnloadCallbacks.put(module, moduleCallbacks);
    }

    @Override
    public TabNMSManager getNmsManager() {
        return this.nmsManager;
    }
}
