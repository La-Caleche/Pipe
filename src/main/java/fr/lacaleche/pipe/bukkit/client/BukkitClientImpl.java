package fr.lacaleche.pipe.bukkit.client;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.client.interfaces.BukkitClient;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitClientImpl extends ClientImpl implements BukkitClient {

    private Player cachedPlayer;

    public BukkitClientImpl(UUID uuid, String username) {
        super(uuid, username);
        this.cachedPlayer = Pipe.getBukkit().getPlugin().getServer().getPlayer(uuid);
        this.save();
    }

    @Override
    public Player getPlayer() {
        if (this.cachedPlayer == null)
            this.cachedPlayer = Pipe.getBukkit().getPlugin().getServer().getPlayer(this.getUUID());
        return this.cachedPlayer;
    }

    @Override
    public boolean isOnline() {
        Player player = this.getPlayer();
        boolean online = player != null && player.isOnline();
        if (!online && player != null)
            this.cachedPlayer = null;
        return online;
    }

    @Override
    public boolean expire() {
        if (this.isOnline()) {
            Logger.info("Trying to expire a client that is still online. Username: %s, UUID: %s", this.getUsername(), this.getUUID().toString());
            return false;
        }
        this.cachedPlayer = null;
        return super.expire();
    }
}
