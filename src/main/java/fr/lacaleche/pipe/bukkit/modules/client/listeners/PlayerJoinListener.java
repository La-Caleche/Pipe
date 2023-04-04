package fr.lacaleche.pipe.bukkit.modules.client.listeners;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.BukkitClientModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Collection;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PipeDebug.eventCalled(event);
        Player player = event.getPlayer();

        Client client = new ModelFilter<ClientImpl>()
            .findOrDefault(
                ClientImpl.class,
                c -> c.getUUID().equals(player.getUniqueId()),
                (queryBuilder) -> queryBuilder.where(new Where("uuid", player.getUniqueId())),
                () -> new ClientImpl(player.getUniqueId(), player.getName())
            );

        Pipe.get().getJoinCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(event, player, client));
    }

}
