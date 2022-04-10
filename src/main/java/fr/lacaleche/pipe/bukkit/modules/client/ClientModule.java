package fr.lacaleche.pipe.bukkit.modules.client;

import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerJoinListener;
import fr.lacaleche.pipe.bukkit.modules.client.listeners.PlayerLeftListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ClientModule extends Module {

    public ClientModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        JavaPlugin plugin = (JavaPlugin) Pipe.get().getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
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
        JavaPlugin plugin = (JavaPlugin) Pipe.get().getPlugin();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        if (players.size() == 0) return;

        Logger.info("Removing clients for %d players...".formatted(players.size()));

        for (Player player : players) {
            Client client = new ModelFilter<ClientImpl>().find(ClientImpl.class, c -> c.getUUID().equals(player.getUniqueId()));
            client.expireNow();
        }
    }

    @Override
    public void registerListeners() {
        Pipe.get().getListenerManager().registerBukkitListener(this, new PlayerJoinListener());
        Pipe.get().getListenerManager().registerBukkitListener(this, new PlayerLeftListener());
    }
}
