package fr.lacaleche.pipe.common.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;
import fr.lacaleche.pipe.common.utils.server.PipeServer;
import fr.lacaleche.pipe.common.utils.server.PipeServerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Packet(name = "ServerListPacket")
@Serializer(variables = {"player"})
public class ServerListPacket extends TransactionalPacket {

    private UUID player;

    public ServerListPacket() {}

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
