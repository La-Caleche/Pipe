package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.util.HashMap;
import java.util.Map;

@Packet(name = "RegisterResourcePacksPacket")
@Serializer(variables = {"resourcePacks"})
public class RegisterResourcePacksPacket extends PacketImpl {

    private final Map<String, String> resourcePacks;

    public RegisterResourcePacksPacket() {
        this.resourcePacks = new HashMap<>();
    }

    public RegisterResourcePacksPacket(Map<String, String> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public Map<String, String> getResourcePacks() {
        return resourcePacks;
    }
}
