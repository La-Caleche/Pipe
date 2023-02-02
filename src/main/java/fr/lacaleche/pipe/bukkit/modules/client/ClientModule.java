package fr.lacaleche.pipe.bukkit.modules.client;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerJoinListener;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerLeftListener;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@AModule(target = ModuleTarget.BUKKIT)
public class ClientModule extends BukkitModule {

    public ClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        JavaPlugin plugin = (JavaPlugin) Pipe.get().getPlugin();

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

    @Override
    public void onDisable() {
        JavaPlugin plugin = (JavaPlugin) Pipe.get().getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Removing clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            Client client = Pipe.get().getClient(player.getUniqueId());
            client.expireNow();
        }

        List<RankImpl> cachedRanks = new ArrayList<RankImpl>(CalecheCore.get().getModelManager().get(RankImpl.class));
        Logger.customDebug("Removing %s ranks from cache...".formatted(cachedRanks.size()));
        cachedRanks.forEach(RankImpl::expireNow);
    }

    @Override
    public void onReload() {
        this.onDisable();
        this.onEnable();
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = (BukkitPipeListenerManager) Pipe.get().getListenerManager();
        bukkitManager.registerBukkitListener(this, new PlayerJoinListener());
        bukkitManager.registerBukkitListener(this, new PlayerLeftListener());
    }
}
