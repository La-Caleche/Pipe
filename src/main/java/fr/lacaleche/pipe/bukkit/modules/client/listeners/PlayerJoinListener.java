package fr.lacaleche.pipe.bukkit.modules.client.listeners;

import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        new ModelFilter<ClientImpl>()
            .findWithSqlOrDefault(
                ClientImpl.class,
                c -> c.getUUID().equals(player.getUniqueId()),
                (queryBuilder) -> queryBuilder.where(new Where("uuid", player.getUniqueId())),
                () -> new ClientImpl(player.getUniqueId())
            );

        event.joinMessage(null);
    }

}
