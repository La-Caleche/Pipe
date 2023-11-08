package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "UnregisterNewServerPacket")
@Serializer(variables = {"app", "host"})
public class UnregisterServerPacket extends PacketImpl {

    private String app;
    private String host;

    public UnregisterServerPacket() {
    }

    public UnregisterServerPacket(String app, String host) {
        this.app = app;
        this.host = host;
    }

    public String getApp() {
        return app;
    }

    public String getHost() {
        return host;
    }

}
