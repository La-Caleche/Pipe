package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.util.UUID;

@Packet(name = "ConnectPacket")
@Serializer(variables = {"player", "server"})
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

    public UUID getPlayer() {
        return player;
    }

    public String getServer() {
        return server;
    }

}
