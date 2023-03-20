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
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.pipe.common.utils.server.PipeServer;
import fr.lacaleche.pipe.common.utils.server.PipeServerImpl;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "ServerListPacket")
public class ServerListPacket extends TransactionalPacket {

    public ServerListPacket() {}

    public ServerListPacket(Token token) {
        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public ServerListPacket(Resolve<Object> resolve, Reject<Object> reject) {
        this.setResponse(new ArrayList<>());
        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
        this.setPacketType(PacketType.REQUEST);
    }
    
    @Override
    public void read(IPacketData data) {
        this.setToken(new Token(data.next()));
        this.setPacketType(PacketType.valueOf(data.next()));

        if (this.getPacketType() == PacketType.REQUEST) return;

        try {
            this.setResponse(this.parseJson(new ObjectMapper().readValue(data.<String>next(), JsonNode.class)));
        } catch (JsonProcessingException e) {
            SentryAPIImpl.getInstance().captureException(e);
        }

        this.setResult(TransactionResult.valueOf(data.next()));
    }

    private List<PipeServer> parseJson(JsonNode jsonNode) {
        List<PipeServer> servers = new ArrayList<>();
        ArrayNode arrayNode = jsonNode.withArray("servers");
        arrayNode.forEach(elem -> {
            PipeServer serverInfo = new PipeServerImpl(
                    elem.get("app").asText(),
                    elem.get("host").asText(),
                    elem.get("serverIcon").asText(),
                    elem.get("maxPlayers").asInt(),
                    elem.get("devServer").asBoolean()
            );
            serverInfo.setOnlinePlayers(elem.get("onlinePlayers").asInt());
            serverInfo.setOnline(elem.get("online").asBoolean());

            servers.add(serverInfo);
        });
        return servers;
    }

    @Override
    public String write() {
        if (this.getPacketType() == PacketType.REQUEST) {
            Core.get().getTransactionManager().registerTransaction(new Transaction(this, this.getToken(), this.getResolve(), this.getReject()));

            this.buildDefault();

            return getBuilder().toString();
        }

        buildDefault().build(getResponse());

        if (this.getPacketType() == PacketType.ANSWER) {
            getBuilder().build(this.getResult());
        }

        return getBuilder().toString();
    }

}
