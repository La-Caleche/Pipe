package fr.lacaleche.pipe.bukkit.modules.command.listeners;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Client client = Pipe.getBukkit().getClient(event.getPlayer());
        Pipe.getBukkit().setLastLocation(client, event.getFrom());
    }

}
