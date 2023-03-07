package fr.lacaleche.pipe.common.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.core.utils.serializer.interfaces.CoreSerializer;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private List<Server> parseJson(JsonNode jsonNode) {
        List<Server> servers = new ArrayList();
        ArrayNode arrayNode = jsonNode.withArray("servers");
        arrayNode.forEach(elem -> {
            Server serverInfo = new Server(elem.get("serverName").asText());
            serverInfo.setServerIcon(elem.get("serverIcon").asText());
            serverInfo.setOnlinePlayers(elem.get("onlinePlayers").asInt());
            serverInfo.setOnline(elem.get("online").asBoolean());

            servers.add(serverInfo);
        });
        return servers;
    }

    @Override
    public String write() {
        if (this.getPacketType() == PacketType.REQUEST) {
            CalecheCore.get().getTransactionManager().registerTransaction(new Transaction(this, this.getToken(), this.getResolve(), this.getReject()));

            this.buildDefault();

            return getBuilder().toString();
        }

        buildDefault().build(getResponse());

        if (this.getPacketType() == PacketType.ANSWER) {
            getBuilder().build(this.getResult());
        }

        return getBuilder().toString();
    }

    @Serializer(variables = {"serverName", "online", "onlinePlayers", "serverIcon"})
    public static class Server {

        private final String serverName;
        private boolean online;
        private int onlinePlayers;
        private String serverIcon;

        public Server(String serverName) {
            this.serverName = serverName;
        }

        public String getServerName() {
            return serverName;
        }

        public String getServerIcon() {
            return serverIcon;
        }

        public int getOnlinePlayers() {
            return onlinePlayers;
        }

        public boolean isOnline() {
            return online;
        }

        public void setServerIcon(String serverIcon) {
            this.serverIcon = serverIcon;
        }

        public void setOnlinePlayers(int onlinePlayers) {
            this.onlinePlayers = onlinePlayers;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

    }

}
