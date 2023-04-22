package fr.lacaleche.pipe.bukkit.modules.client.listeners;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.client.BukkitClientModule;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ModelSavedListener implements CoreListener {

    private BukkitClientModule clientModule;

    public ModelSavedListener(BukkitClientModule clientModule) {
        this.clientModule = clientModule;
    }

    @PacketReader(packet = ModelSavedPacket.class)
    public void onModelUpdated(PacketReadEvent event, ModelSavedPacket packet) {
        if (Pipe.get().getTabManager() != null && packet.getResourceClass().equals(ClientImpl.class.getName())) {
            Client client = new ModelFilter<ClientImpl>().model(ClientImpl.class)
                    .cache((model) -> model.getId() == packet.getResourceId())
                    .sql((sql) -> sql.where(new Where("id", packet.getResourceId()))).getOne();

            Plugin plugin = Pipe.get().getPlugin();
            Player player = plugin.getServer().getPlayer(client.getUUID());
            if (player == null) return;

            client.refresh();

            TabManager tabManager = Pipe.get().getTabManager();
            TabPlayer tabPlayer = tabManager.getTabPlayer(player.getUniqueId());

            if (!client.isStaff() && tabPlayer.getNameTag().hasLine(1)) {
                tabPlayer.getNameTag().removeLine(1);
            } else if (client.isStaff() && !tabPlayer.getNameTag().hasLine(1)) {
                tabPlayer.getNameTag().addLine("<rank>", 1);
            }

            tabManager.refreshPlayer(tabPlayer);
        }
    }

}
