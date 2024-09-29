package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

@Packet(name = "RegisterNewServerPacket")
@Serializer(variables = {"app", "host", "serverIcon", "maxPlayers", "devServer"})
public class RegisterServerPacket extends PacketImpl {

    private String app;
    private String host;
    private String serverIcon;
    private int maxPlayers;
    boolean devServer;

    public RegisterServerPacket() {
    }

    public RegisterServerPacket(String app, String host, String serverIcon, int maxPlayers, boolean devServer) {
        this.app = app;
        this.host = host;
        this.serverIcon = serverIcon;
        this.maxPlayers = maxPlayers;
        this.devServer = devServer;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getApp() {
        return app;
    }

    public String getServerIcon() {
        return serverIcon;
    }

    public String getHost() {
        return host;
    }

    public boolean isDevServer() {
        return devServer;
    }

}
