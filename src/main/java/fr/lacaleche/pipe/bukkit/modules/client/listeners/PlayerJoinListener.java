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
                .model((Class<ClientImpl>) Pipe.clientClass())
                .cache(c -> c.getUUID().equals(player.getUniqueId()))
                .sql((sql) -> sql.where(new Where("uuid", player.getUniqueId())))
                .def(() -> (ClientImpl) Pipe.get().instanciateClient(player.getUniqueId(), player.getName())).getOne();

        client.cancelExpiration();

        Pipe.getBukkit().getJoinCallbacks().values().stream()
                .flatMap(Collection::stream)
                .forEach(callback -> callback.accept(event, player, client));
    }

}
