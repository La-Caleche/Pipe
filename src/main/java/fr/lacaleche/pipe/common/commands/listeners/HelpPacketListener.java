package fr.lacaleche.pipe.common.commands.listeners;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class HelpPacketListener implements CoreListener {

    @PacketReader(packet = HelpPacket.class)
    public void onHelpPacket(PacketReadEvent event, HelpPacket packet) {
        if (packet.getPacketType() == PacketType.REQUEST) {
            if (Pipe.get().getCommandManager().isRegistered(packet.getCommand())) {
                TextComponent.Builder formatted = new HelperImpl(packet.getLocale(), packet.getCommand()).format();
                HelpPacket responsePacket = new HelpPacket(packet.getCommand(), packet.getLocale(), packet.getToken());
                responsePacket.setResult(TransactionResult.ACCEPT);
                responsePacket.setResponse(GsonComponentSerializer.gson().serialize(formatted.asComponent()));
                CalecheCore.get().getPacketManager().publish(responsePacket);
            }
        } else if (packet.getPacketType() == PacketType.ANSWER) {
            Transaction transaction = (Transaction) CalecheCore.get().getTransactionManager().getTransactions().get(packet.getToken().toString());
            if (transaction == null) return;

            transaction.setResponse(GsonComponentSerializer.gson().deserialize(packet.getResponse()));
            transaction.setResult(packet.getResult());
            CalecheCore.get().getTransactionManager().callTransaction(transaction);
        }
    }

}
