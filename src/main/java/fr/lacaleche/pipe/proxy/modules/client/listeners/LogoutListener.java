package fr.lacaleche.pipe.proxy.modules.client.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import fr.lacaleche.pipe.proxy.modules.client.ClientModule;

public class LogoutListener {

    @Subscribe
    public void onPlayerLeft(DisconnectEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = Pipe.get().getClient(player.getUniqueId());
        client.expireIn(5 * 60 * 1000);

        CalecheCore.get().getCentralModuleManager().getModule(ClientModule.class).getQuitCallbacks().forEach(callback -> callback.accept(event, player, client));
    }

}
