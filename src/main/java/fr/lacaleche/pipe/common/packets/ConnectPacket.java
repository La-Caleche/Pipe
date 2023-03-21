package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;

import java.util.UUID;

@Packet(name = "ConnectPacket")
public class ConnectPacket extends TransactionalPacket {

    private UUID player;
    private String server;

    public ConnectPacket() {
    }

    public ConnectPacket(UUID player, String server, Token token) {
        this.player = player;
        this.server = server;

        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public ConnectPacket(UUID player, String server, Resolve<Object> resolve, Reject<Object> reject) {
        this.player = player;
        this.server = server;

        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
        this.setResponse("none");
        this.setPacketType(PacketType.REQUEST);
    }
    
    @Override
    public void read(IPacketData data) {
        this.setToken(new Token(data.next()));
        this.setPacketType(PacketType.valueOf(data.next()));

        this.player = UUID.fromString(data.next());
        this.server = data.next();

        if (this.getPacketType() == PacketType.ANSWER && data.hasNext()) {
            this.setResponse(data.next());
            this.setResult(TransactionResult.valueOf(data.next()));
        }
    }

    public UUID getPlayer() {
        return player;
    }

    public String getServer() {
        return server;
    }

    @Override
    public String write() {
        buildDefault().build(this.player).build(this.server).build(this.getResponse());

        if (getPacketType() == PacketType.REQUEST) {
            Core.get().getTransactionManager().registerTransaction(new Transaction(this, this.getToken(), this.getResolve(), this.getReject()));
        }

        if (this.getPacketType() == PacketType.ANSWER) {
            getBuilder().build(this.getResult());
        }

        return getBuilder().toString();
    }

}
