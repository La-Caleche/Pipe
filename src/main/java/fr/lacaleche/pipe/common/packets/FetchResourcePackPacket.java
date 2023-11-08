package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

@Packet(name = "FetchResourcePackPacket")
@Serializer(variables = {"name"})
public class FetchResourcePackPacket extends TransactionalPacket {

    private String name;

    public FetchResourcePackPacket() {
    }

    public FetchResourcePackPacket(String name, Token token) {
        this.name = name;

        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public FetchResourcePackPacket(String name, Resolve<Object> resolve, Reject<Object> reject) {
        this.name = name;

        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
        this.setResponse("none");
        this.setPacketType(PacketType.REQUEST);
    }

    public String getName() {
        return name;
    }

}
