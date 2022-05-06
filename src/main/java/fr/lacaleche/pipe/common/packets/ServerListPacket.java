package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.core.utils.serializer.interfaces.CoreSerializer;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "ServerListPacket")
public class ServerListPacket extends PacketImpl {

    private List<ServerInfo> servers;
    private Token token;
    private Resolve<Object> resolve;
    private Reject<Object> reject;
    private PacketType packetType;

    public ServerListPacket() {}

    public ServerListPacket(List<ServerInfo> servers, Token token) {
        this.servers = servers;
        this.token = token;
        this.packetType = PacketType.ANSWER;
    }

    public ServerListPacket(Resolve<Object> resolve, Reject<Object> reject) {
        this.token = new Token(64);
        this.servers = new ArrayList<>();
        this.resolve = resolve;
        this.reject = reject;
        this.packetType = PacketType.REQUEST;
    }
    
    @Override
    public void read(String[] data) {
        this.token = new Token(data[1]);
        this.packetType = PacketType.valueOf(data[2]);
        this.servers = parseJson(new JSONObject(data[3]).getJSONArray("servers"));
    }

    public List<ServerInfo> getServers() {
        return servers;
    }

    public Token getToken() {
        return token;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    private List<ServerInfo> parseJson(JSONArray data) {
        List<ServerInfo> servers = new ArrayList();
        data.forEach(elem -> {
            JSONObject object = (JSONObject) elem;
            ServerInfo serverInfo = new ServerInfo(object.getString("serverName"));
            serverInfo.setServerIcon(object.getString("serverIcon"));
            serverInfo.setOnlinePlayers(object.getInt("onlinePlayers"));
            serverInfo.setOnline(object.getBoolean("online"));

            servers.add(serverInfo);
        });
        return servers;
    }

    @Override
    public String write() {
        String data = null;

        JSONObject serverInfos = new JSONObject();
        JSONArray array = new JSONArray();
        this.servers.forEach(serverInfo -> array.put(new JSONObject(CoreSerializer.get().serialize(serverInfo).get())));
        serverInfos.put("servers", array);

        data = getBuilder().build(id()).build(this.token).build(this.packetType).build(serverInfos.toString()).toString();

        if (this.packetType == PacketType.REQUEST) {
            CalecheCore.get().getTransactionManager().registerTransaction(new Transaction(this, token, resolve, reject));
        }

        return data;
    }

    @Serializer(variables = {"serverName", "online", "onlinePlayers", "serverIcon"})
    public static class ServerInfo {

        private final String serverName;
        private boolean online;
        private int onlinePlayers;
        private String serverIcon;

        public ServerInfo(String serverName) {
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
