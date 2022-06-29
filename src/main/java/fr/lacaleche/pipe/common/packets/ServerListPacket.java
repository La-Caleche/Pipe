package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.core.utils.serializer.interfaces.CoreSerializer;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

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
        this.setResponse(this.parseJson(new JSONObject(data.<String>next()).getJSONArray("servers")));
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
        List<String> servers = this.getResponse();

        JSONObject serverInfos = new JSONObject();
        JSONArray array = new JSONArray();

        servers.forEach(serverInfo -> array.put(new JSONObject(CoreSerializer.get().serialize(serverInfo).get())));
        serverInfos.put("servers", array);

        buildDefault().build(serverInfos.toString());

        if (this.getPacketType() == PacketType.REQUEST) {
            CalecheCore.get().getTransactionManager().registerTransaction(new Transaction(this, this.getToken(), this.getResolve(), this.getReject()));
        }

        return getBuilder().toString();
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
