package fr.lacaleche.pipe.bukkit.client.interfaces;

import fr.lacaleche.pipe.common.clients.Client;
import org.bukkit.entity.Player;

public interface BukkitClient extends Client {

    boolean isOnline();

    Player getPlayer();

}
