package fr.lacaleche.pipe.proxy.modules.client;

import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerInfo;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
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
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.commands.helper.interfaces.Helper;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;
import fr.lacaleche.pipe.common.packets.ServerListPacket;
import fr.lacaleche.pipe.proxy.ProxyPlugin;
import fr.lacaleche.pipe.proxy.commands.ProxyCommandManager;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipe.proxy.modules.client.listeners.LoginListener;
import fr.lacaleche.pipe.proxy.modules.client.listeners.LogoutListener;
import fr.lacaleche.pipe.proxy.modules.client.listeners.ModelSavedListener;
import fr.lacaleche.pipe.proxy.modules.client.listeners.PermissionsPacketListener;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AModule(target = ModuleTarget.PROXY)
public class ProxyClientModule extends Module {

    private List<TriConsumer<PostLoginEvent, Player, Client>> joinCallbacks;
    private List<TriConsumer<DisconnectEvent, Player, Client>> quitCallbacks;

    private List<TriConsumer<ServerConnectedEvent, Player, Client>> joinServerCallbacks;

    public ProxyClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        ProxyPlugin plugin = Pipe.get().getPlugin();
        this.joinCallbacks = new ArrayList<>();
        this.joinServerCallbacks = new ArrayList<>();
        this.quitCallbacks = new ArrayList<>();

        this.addJoinServerCallback(this::loadCommandsFor);

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
        this.joinServerCallbacks.clear();
        this.quitCallbacks.clear();
    }

    @Override
    public void registerListeners() {
        ProxyPipeListenerManager proxyManager = Pipe.get().getListenerManager();
        proxyManager.registerProxyListener(this, new LoginListener());
        proxyManager.registerProxyListener(this, new LogoutListener());
        proxyManager.registerCustomListener(this, new PermissionsPacketListener());
        proxyManager.registerCustomListener(this, new ModelSavedListener(this));
    }

    @Override
    public void registerPackets() {
        CalecheCore.get().getPacketManager().registerPacket(CheckPermissionsPacket.class);
        CalecheCore.get().getPacketManager().registerPacket(ModelSavedPacket.class);
    }

    public void loadCommandsFor(ServerConnectedEvent event, Player player, Client client) {
        ProxyCommandManager proxyCommandManager = (ProxyCommandManager) Pipe.get().getCommandManager();
        client.allowedCommands().clear();

        ServerInfo info = null;
        if (event != null) info = event.getServer().getServerInfo();

        CheckPermissionsPacket packet = new CheckPermissionsPacket(proxyCommandManager.getNetworkCommandsForPlayer(player, info), player.getUniqueId(), response -> {
            List<CheckPermissionsPacket.AllowedCommand> commands = (List<CheckPermissionsPacket.AllowedCommand>) response;
            commands.forEach(allowedCommand -> {
                if (allowedCommand.isAllowed()) client.addAllowedCommand(allowedCommand.getCommand());
            });
        }, reject -> {});
        CalecheCore.get().getPacketManager().publish(packet);

        proxyCommandManager.getCommands().forEach((s, minecraftCommandClass) -> {
            Helper helper = new HelperImpl(client.getLocale(), s);
            if (helper.senderCanUseCommand(player)) {
                client.addAllowedCommand(s);
            }
        });
    }

    public void addJoinCallback(TriConsumer<PostLoginEvent, Player, Client> callback) {
        this.joinCallbacks.add(callback);
    }

    public void addJoinServerCallback(TriConsumer<ServerConnectedEvent, Player, Client> callback) {
        this.joinServerCallbacks.add(callback);
    }

    public void addQuitCallbacks(TriConsumer<DisconnectEvent, Player, Client> callback) {
        this.quitCallbacks.add(callback);
    }

    public List<TriConsumer<PostLoginEvent, Player, Client>> getJoinCallbacks() {
        return joinCallbacks;
    }

    public List<TriConsumer<ServerConnectedEvent, Player, Client>> getJoinServerCallbacks() {
        return joinServerCallbacks;
    }

    public List<TriConsumer<DisconnectEvent, Player, Client>> getQuitCallbacks() {
        return quitCallbacks;
    }
}
