package fr.lacaleche.pipe.bukkit.modules.client;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.utils.commons.consumers.TriConsumer;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.ModelSavedListener;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerJoinListener;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerLeftListener;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@AModule(target = ModuleTarget.BUKKIT)
public class BukkitClientModule extends BukkitModule {

    public BukkitClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        Pipe pipe = Pipe.get();
        Plugin plugin = pipe.getPlugin();

        pipe.addJoinCallback(this, (listener, player, client) -> {
            if (listener == null) return;
            listener.joinMessage(null);

            plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
                Client onlineClient = Pipe.get().getClient(onlinePlayer.getUniqueId());
                onlinePlayer.sendMessage(onlineClient.getLocale().t("global.player_join").arg("player", player.getName()).ph("player", player).ct());
            });
        });

        pipe.addQuitCallbacks(this, (listener, player, client) -> {
            if (listener == null) return;
            listener.quitMessage(null);

            plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
                Client onlineClient = Pipe.get().getClient(onlinePlayer.getUniqueId());
                onlinePlayer.sendMessage(onlineClient.getLocale().t("global.player_quit").arg("player", player.getName()).ph("player", player).ct());
            });
        });
    }

    @Override
    public void onDisable() {
        Pipe pipe = Pipe.get();
        JavaPlugin plugin = pipe.getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Removing clients for %d players...", players.size());

        for (Player player : players) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            pipe.getQuitCallbacks().values().stream()
                    .flatMap(Collection::stream)
                    .forEach(callback -> callback.accept(null, player, client));
            client.expireNow();
        }

        List<RankImpl> cachedRanks = new ArrayList<RankImpl>(Core.get().getModelManager().get(RankImpl.class));
        Logger.customDebug("Removing %s ranks from cache...", cachedRanks.size());
        cachedRanks.forEach(RankImpl::expireNow);

        pipe.removeJoinCallbacks(this);
        pipe.removeQuitCallbacks(this);
    }

    @Override
    public void onEnableFinish() {
        Pipe pipe = Pipe.get();
        JavaPlugin plugin = pipe.getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Loading clients for %d players...", players.size());

        for (Player player : players) {
            new ModelFilter<ClientImpl>()
                    .model(ClientImpl.class)
                    .cache(c -> c.getUUID().equals(player.getUniqueId()))
                    .sql((sql) -> sql.where(new Where("uuid", player.getUniqueId())))
                    .def(() -> new ClientImpl(player.getUniqueId(), player.getName())).getOne();
        }

        Logger.customDebug("Calling join callbacks for %d players...", players.size());

        for (Player player : players) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            pipe.getJoinCallbacks().values().stream()
                    .flatMap(Collection::stream)
                    .forEach(callback -> callback.accept(null, player, client));
        }

        Logger.customDebug("Join callbacks called for %d players.", players.size());
    }

    @Override
    public void registerPackets() {
        Core.get().getPacketManager().registerPacket(ModelSavedPacket.class);
    }

    @Override
    public void registerPlaceholders() {
        PipeText text = Pipe.get().text();

        text.registerPlaceHolder("rank", (placeHolderArguments, locale) -> {
            if (placeHolderArguments.next() instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                return client.getRank().getColoredRankName(locale);
            }
            return Component.text("Unknown").color(NamedTextColor.GRAY);
        });

        text.registerPlaceHolder("player", (placeHolderArguments, locale) -> {
            if (placeHolderArguments.next() instanceof Player player) {
                Client client = Pipe.get().getClient(player.getUniqueId());
                return client.getRank().colorize(player.getName());
            }
            return Component.text("Unknown").color(NamedTextColor.GRAY);
        });
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new PlayerJoinListener());
        bukkitManager.registerBukkitListener(this, new PlayerLeftListener());
        bukkitManager.registerCustomListener(this, new ModelSavedListener(this));
    }
}
