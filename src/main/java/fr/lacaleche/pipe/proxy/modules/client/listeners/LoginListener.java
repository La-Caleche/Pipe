package fr.lacaleche.pipe.proxy.modules.client.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import fr.lacaleche.pipe.proxy.modules.client.ProxyClientModule;

import java.util.Optional;

public class LoginListener {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = new ModelFilter<ClientImpl>()
            .findOrDefault(
                ClientImpl.class,
                c -> c.getUUID().equals(player.getUniqueId()),
                (queryBuilder) -> queryBuilder.where(new Where("uuid", player.getUniqueId())),
                () -> new ClientImpl(player.getUniqueId())
            );

        CalecheCore.get().getCentralModuleManager().getModule(ProxyClientModule.class).getJoinCallbacks().forEach(callback -> callback.accept(event, player, client));
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        Client client = Pipe.get().getClient(player.getUniqueId());
        CalecheCore.get().getCentralModuleManager().getModule(ProxyClientModule.class).getJoinServerCallbacks().forEach(callback -> callback.accept(event, player, client));
    }

}
