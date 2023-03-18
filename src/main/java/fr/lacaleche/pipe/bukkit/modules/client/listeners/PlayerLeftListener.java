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

public class PlayerLeftListener implements Listener {

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = Pipe.get().getClient(player.getUniqueId());
        client.expireIn(5 * 60 * 1000);

        Core.getModule(BukkitClientModule.class).getQuitCallbacks().forEach(callback -> callback.accept(event, player, client));
    }

}
