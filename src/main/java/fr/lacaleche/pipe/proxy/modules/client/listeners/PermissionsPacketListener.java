package fr.lacaleche.pipe.proxy.modules.client.listeners;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;

public class PermissionsPacketListener implements CoreListener {

    @PacketReader(packet = CheckPermissionsPacket.class)
    public void onHelpPacket(PacketReadEvent event, CheckPermissionsPacket packet) {
        if (packet.getPacketType() == PacketType.ANSWER) {
            Transaction transaction = (Transaction) Core.get().getTransactionManager().getTransactions().get(packet.getToken().toString());
            if (transaction == null) return;

            transaction.setResponse(packet.getResponse());
            transaction.setResult(packet.getResult());
            Core.get().getTransactionManager().callTransaction(transaction);
        }
    }

}
