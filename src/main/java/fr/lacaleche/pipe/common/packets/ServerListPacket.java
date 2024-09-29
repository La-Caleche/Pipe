package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.util.UUID;

@Packet(name = "ServerListPacket")
@Serializer(variables = {"player"})
public class ServerListPacket extends TransactionalPacket {

    private UUID player;

    public ServerListPacket() {
    }

    public ServerListPacket(Token token) {
        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public ServerListPacket(UUID player, Resolve<Object> resolve, Reject<Object> reject) {
        this.player = player;

        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
        this.setPacketType(PacketType.REQUEST);
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public UUID getPlayer() {
        return player;
    }

}
