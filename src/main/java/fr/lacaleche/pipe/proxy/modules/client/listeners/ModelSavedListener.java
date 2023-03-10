package fr.lacaleche.pipe.proxy.modules.client.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.BukkitClientModule;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.proxy.ProxyPlugin;
import fr.lacaleche.pipe.proxy.modules.client.ProxyClientModule;

public class ModelSavedListener implements CoreListener {

    private ProxyClientModule clientModule;

    public ModelSavedListener(ProxyClientModule clientModule) {
        this.clientModule = clientModule;
    }

    @PacketReader(packet = ModelSavedPacket.class)
    public void onModelUpdated(PacketReadEvent event, ModelSavedPacket packet) {
        if (packet.buildResource() instanceof ClientImpl client) {
            ProxyPlugin plugin = Pipe.get().getPlugin();
            Player player = plugin.getServer().getPlayer(client.getUUID()).orElse(null);
            if (player == null) return;

            client.refresh();
            clientModule.loadCommandsFor(null, player, client);
        }
    }

}
