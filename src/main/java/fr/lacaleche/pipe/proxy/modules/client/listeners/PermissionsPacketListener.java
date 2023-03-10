package fr.lacaleche.pipe.proxy.modules.client.listeners;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class PermissionsPacketListener implements CoreListener {

    @PacketReader(packet = CheckPermissionsPacket.class)
    public void onHelpPacket(PacketReadEvent event, CheckPermissionsPacket packet) {
        if (packet.getPacketType() == PacketType.ANSWER) {
            Transaction transaction = (Transaction) CalecheCore.get().getTransactionManager().getTransactions().get(packet.getToken().toString());
            if (transaction == null) return;

            transaction.setResponse(packet.getResponse());
            transaction.setResult(packet.getResult());
            CalecheCore.get().getTransactionManager().callTransaction(transaction);
        }
    }

}
