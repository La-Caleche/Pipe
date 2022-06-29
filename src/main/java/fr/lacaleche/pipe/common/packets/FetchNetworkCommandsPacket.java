package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;

@Packet(name = "FetchNetworkCommandsPacket")
public class FetchNetworkCommandsPacket extends PacketImpl {

    public FetchNetworkCommandsPacket() {
    }

    @Override
    public String write() {
        return buildDefault().toString();
    }

}
