package fr.lacaleche.pipe.bukkit.modules.client.listeners;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.BukkitClientModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;

public class PlayerLeftListener implements Listener {

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = Pipe.get().getClient(player.getUniqueId());

        Pipe.get().getQuitCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(event, player, client));

        client.expireIn(10 * 1000);
    }

}
