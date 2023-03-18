package fr.lacaleche.pipe.bukkit.modules.client.listeners;

import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.BukkitClientModule;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import me.neznamy.tab.api.TabPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ModelSavedListener implements CoreListener {

    private BukkitClientModule clientModule;

    public ModelSavedListener(BukkitClientModule clientModule) {
        this.clientModule = clientModule;
    }

    @PacketReader(packet = ModelSavedPacket.class)
    public void onModelUpdated(PacketReadEvent event, ModelSavedPacket packet) {
        if (Pipe.get().getTabManager() != null && packet.buildResource() instanceof ClientImpl client) {
            Plugin plugin = Pipe.get().getPlugin();
            Player player = plugin.getServer().getPlayer(client.getUUID());
            if (player == null) return;
            TabPlayer tabPlayer = Pipe.get().getTabManager().getTabAPI().getPlayer(client.getUUID());
            if (tabPlayer == null) return;

            client.refresh();
            clientModule.loadTab(tabPlayer, client);
        }
    }

}
