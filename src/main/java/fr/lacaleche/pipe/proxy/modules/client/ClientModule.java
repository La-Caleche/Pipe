package fr.lacaleche.pipe.proxy.modules.client;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.clients.ranks.RankImpl;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipe.proxy.modules.client.listeners.LoginListener;
import fr.lacaleche.pipe.proxy.modules.client.listeners.LogoutListener;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AModule(target = ModuleTarget.BUNGEE)
public class ClientModule extends Module {

    public ClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        Plugin plugin = Pipe.get().getPlugin();

        Collection<? extends ProxiedPlayer> players = plugin.getProxy().getPlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Loading clients for %d players...".formatted(players.size()));

        for (ProxiedPlayer player : players) {
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
        Plugin plugin = Pipe.get().getPlugin();

        Collection<? extends ProxiedPlayer> players = plugin.getProxy().getPlayers();
        if (players.size() == 0) return;

        Logger.customDebug("Removing clients for %d players...".formatted(players.size()));

        for (ProxiedPlayer player : players) {
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
        ProxyPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerProxyListener(this, new LoginListener());
        bukkitManager.registerProxyListener(this, new LogoutListener());
    }
}
