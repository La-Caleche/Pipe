package fr.lacaleche.pipe.bukkit.modules.client;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.ModelSavedListener;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerJoinListener;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerLeftListener;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.common.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import me.neznamy.tab.api.TabPlayer;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.BiConsumer;

@AModule(target = ModuleTarget.BUKKIT)
public class BukkitClientModule extends BukkitModule {

    private List<TriConsumer<PlayerJoinEvent, Player, Client>> joinCallbacks;
    private List<TriConsumer<PlayerQuitEvent, Player, Client>> quitCallbacks;

    public BukkitClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        JavaPlugin plugin = Pipe.get().getPlugin();
        this.joinCallbacks = new ArrayList<>();
        this.quitCallbacks = new ArrayList<>();

        this.addJoinCallback((listener, player, client) -> {
            if (listener == null) return;
            listener.joinMessage(client.getLocale().t("global.player_join").arg("player", player.getName()).arg("rank", client.getRank().translatedName(client.getLocale())).arg("color", client.getRank().getColorCode()).ct());
        });

        this.addJoinCallback((listener, player, client) -> {
            Pipe.get().getTaskManager().newTask(new TaskBuilder().callback(task -> {
                TabManager tabManager = Pipe.get().getTabManager();
                TabPlayer tabPlayer = tabManager.getTabAPI().getPlayer(player.getUniqueId());

                if (tabPlayer == null) {
                    task.retryIn(5);
                    return;
                }

                this.loadTab(tabPlayer, client);
            }));
        });

        this.addQuitCallbacks((listener, player, client) -> {
            if (listener == null) return;
            listener.quitMessage(client.getLocale().t("global.player_quit").arg("player", player.getName()).arg("rank", client.getRank().translatedName(client.getLocale())).arg("color", client.getRank().getColorCode()).ct());
        });

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Loading clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            new ModelFilter<ClientImpl>()
                .findOrDefault(
                        ClientImpl.class,
                        c -> c.getUUID().equals(player.getUniqueId()),
                        (queryBuilder) -> queryBuilder.where(new Where("uuid", player.getUniqueId())),
                        () -> new ClientImpl(player.getUniqueId())
                );
        }
    }

    public void loadTab(TabPlayer tabPlayer, Client client) {
        TabManager tabManager = Pipe.get().getTabManager();

        this.setTabValue(tabPlayer, "pipe.player.display_name", client, tabManager::setName);
        this.setTabValue(tabPlayer, "pipe.player.tab_name", client, tabManager::setTabName);
        this.setTabValue(tabPlayer, "pipe.player.tab_prefix", client, tabManager::setTabPrefix);
        this.setTabValue(tabPlayer, "pipe.player.tab_suffix", client, tabManager::setTabSuffix);
    }

    private void setTabValue(TabPlayer tabPlayer, String key, Client client, BiConsumer<TabPlayer, Component> consumer) {
        TabManager tabManager = Pipe.get().getTabManager();
        if (!client.getLocale().isTranslated(key)) return;
        consumer.accept(tabPlayer, client.getLocale().t(key).arg("name", tabPlayer.getName()).arg("rank", client.getRank().translatedName(client.getLocale())).arg("color", client.getRank().getColorCode()).ct());
    }

    @Override
    public void onDisable() {
        JavaPlugin plugin = Pipe.get().getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Removing clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            this.quitCallbacks.forEach(callback -> callback.accept(null, player, client));
            client.expireNow();
        }

        List<RankImpl> cachedRanks = new ArrayList<RankImpl>(CalecheCore.get().getModelManager().get(RankImpl.class));
        Logger.customDebug("Removing %s ranks from cache...".formatted(cachedRanks.size()));
        cachedRanks.forEach(RankImpl::expireNow);

        this.joinCallbacks.clear();
        this.quitCallbacks.clear();
    }

    @Override
    public void onEnableFinish() {
        JavaPlugin plugin = Pipe.get().getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Calling join callbacks for %d players...".formatted(players.size()));

        for (Player player : players) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            this.joinCallbacks.forEach(callback -> callback.accept(null, player, client));
        }

        Logger.customDebug("Join callbacks called for %d players.".formatted(players.size()));
    }

    @Override
    public void registerPackets() {
        CalecheCore.get().getPacketManager().registerPacket(ModelSavedPacket.class);
    }

    public void addJoinCallback(TriConsumer<PlayerJoinEvent, Player, Client> callback) {
        this.joinCallbacks.add(callback);
    }

    public void addQuitCallbacks(TriConsumer<PlayerQuitEvent, Player, Client> callback) {
        this.quitCallbacks.add(callback);
    }

    public List<TriConsumer<PlayerJoinEvent, Player, Client>> getJoinCallbacks() {
        return joinCallbacks;
    }

    public List<TriConsumer<PlayerQuitEvent, Player, Client>> getQuitCallbacks() {
        return quitCallbacks;
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new PlayerJoinListener());
        bukkitManager.registerBukkitListener(this, new PlayerLeftListener());
        bukkitManager.registerCustomListener(this, new ModelSavedListener(this));
    }
}
