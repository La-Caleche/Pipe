package fr.lacaleche.pipe.proxy.modules.client.listeners;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LogoutListener implements Listener {

    @EventHandler
    public void onPlayerLeft(PlayerDisconnectEvent event) {
        PipeDebug.eventCalled(event);
        ProxiedPlayer player = event.getPlayer();

        Client client = Pipe.get().getClient(player.getUniqueId());
        client.expireIn(5 * 60 * 1000);
    }

}
