package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "UnregisterNewServerPacket")
public class UnregisterServerPacket extends PacketImpl {

    private String app;
    private String host;

    public UnregisterServerPacket() {
    }

    public UnregisterServerPacket(String app, String host) {
        this.app = app;
        this.host = host;
    }

    @Override
    public void read(IPacketData data) {
        this.app = data.next();
        this.host = data.next();
    }

    public String getApp() {
        return app;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String write() {
        return buildDefault().build(this.app).build(this.host).toString();
    }

}
