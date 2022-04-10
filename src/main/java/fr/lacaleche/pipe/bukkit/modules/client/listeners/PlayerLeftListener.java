package fr.lacaleche.pipe.bukkit.modules.client.listeners;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeftListener implements Listener {

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Client client = new ModelFilter<ClientImpl>().find(ClientImpl.class, c -> c.getUUID().equals(player.getUniqueId()));
        client.expireNow();

        event.quitMessage(null);
    }

}
