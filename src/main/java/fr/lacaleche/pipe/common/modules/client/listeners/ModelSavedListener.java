package fr.lacaleche.pipe.common.modules.client.listeners;

import fr.lacaleche.core.databases.mysql.models.events.ModelSavedEvent;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.events.annotations.CoreEventHandler;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import me.neznamy.tab.api.TabPlayer;

public class ModelSavedListener implements CoreListener {

    @PacketReader(packet = ModelSavedPacket.class)
    public void onModelUpdated(PacketReadEvent event, ModelSavedPacket packet) {
        if (Pipe.get().getTabManager() != null && packet.buildResource() instanceof ClientImpl client) {
            TabPlayer tabPlayer = Pipe.get().getTabManager().getTabAPI().getPlayer(client.getUUID());

            client.refresh();
            client.loadTab(tabPlayer);
        }
    }

}
