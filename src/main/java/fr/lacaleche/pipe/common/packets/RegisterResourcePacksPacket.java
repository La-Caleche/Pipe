package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.HashMap;
import java.util.Map;

@Packet(name = "RegisterResourcePacksPacket")
public class RegisterResourcePacksPacket extends PacketImpl {

    private Map<String, String> resourcePacks;

    public RegisterResourcePacksPacket() {
        this.resourcePacks = new HashMap<>();
    }

    public RegisterResourcePacksPacket(Map<String, String> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public Map<String, String> getResourcePacks() {
        return resourcePacks;
    }

    @Override
    public void read(IPacketData data) {
        int size = Integer.parseInt(data.next());
        for (int i = 0; i < size; i++) {
            this.resourcePacks.put(data.next(), data.next());
        }
    }

    @Override
    public String write() {
        buildDefault().build(this.resourcePacks.size());
        for (Map.Entry<String, String> entry : this.resourcePacks.entrySet()) {
            getBuilder().build(entry.getKey()).build(entry.getValue());
        }
        return getBuilder().toString();
    }
}
