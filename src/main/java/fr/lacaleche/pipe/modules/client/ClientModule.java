package fr.lacaleche.pipe.modules.client;

import fr.lacaleche.base.Minecraft;
import fr.lacaleche.base.clients.Client;
import fr.lacaleche.base.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.modules.client.listeners.PlayerJoinListener;
import fr.lacaleche.pipe.modules.client.listeners.PlayerLeftListener;
import fr.lacaleche.pipe.utils.PipeListenersUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ClientModule extends Module {

    @Override
    public void onEnable() {
        Collection<? extends Player> players = Pipe.get().getPlugin().getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.info("Loading clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            new ModelFilter<ClientImpl>()
                .findWithSqlOrDefault(
                        ClientImpl.class,
                        c -> c.getUUID().equals(player.getUniqueId()),
                        (queryBuilder) -> queryBuilder.where(new Where("uuid", player.getUniqueId())),
                        () -> new ClientImpl(player.getUniqueId())
                );
        }
    }

    @Override
    public void onDisable() {
        Collection<? extends Player> players = Pipe.get().getPlugin().getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.info("Removing clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            Client client = new ModelFilter<ClientImpl>().find(ClientImpl.class, c -> c.getUUID().equals(player.getUniqueId()));
            client.expireNow();
        }
    }

    @Override
    public void registerListeners() {
        PipeListenersUtils.registerNewListener(Pipe.get().getPlugin(), new PlayerJoinListener());
        PipeListenersUtils.registerNewListener(Pipe.get().getPlugin(), new PlayerLeftListener());
    }
}
