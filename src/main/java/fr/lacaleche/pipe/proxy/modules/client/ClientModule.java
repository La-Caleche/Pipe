package fr.lacaleche.pipe.proxy.modules.client;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
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
import fr.lacaleche.pipe.common.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import fr.lacaleche.pipe.proxy.ProxyPlugin;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipe.proxy.modules.client.listeners.LoginListener;
import fr.lacaleche.pipe.proxy.modules.client.listeners.LogoutListener;
import me.neznamy.tab.api.TabPlayer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AModule(target = ModuleTarget.PROXY)
public class ClientModule extends Module {

    private List<TriConsumer<PostLoginEvent, Player, Client>> joinCallbacks;
    private List<TriConsumer<DisconnectEvent, Player, Client>> quitCallbacks;

    public ClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        ProxyPlugin plugin = Pipe.get().getPlugin();
        this.joinCallbacks = new ArrayList<>();
        this.quitCallbacks = new ArrayList<>();

        Collection<? extends Player> players = plugin.getServer().getAllPlayers();
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
        ProxyPlugin plugin = Pipe.get().getPlugin();

        Collection<? extends Player> players = plugin.getServer().getAllPlayers();
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
    public void registerListeners() {
        ProxyPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerProxyListener(this, new LoginListener());
        bukkitManager.registerProxyListener(this, new LogoutListener());
    }

    public void addJoinCallback(TriConsumer<PostLoginEvent, Player, Client> callback) {
        this.joinCallbacks.add(callback);
    }

    public void addQuitCallbacks(TriConsumer<DisconnectEvent, Player, Client> callback) {
        this.quitCallbacks.add(callback);
    }

    public List<TriConsumer<PostLoginEvent, Player, Client>> getJoinCallbacks() {
        return joinCallbacks;
    }

    public List<TriConsumer<DisconnectEvent, Player, Client>> getQuitCallbacks() {
        return quitCallbacks;
    }
}
