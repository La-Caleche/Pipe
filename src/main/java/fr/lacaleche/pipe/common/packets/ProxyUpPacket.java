package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "ProxyUpPacket")
public class ProxyUpPacket extends PacketImpl {

    private List<String> servers;

    public ProxyUpPacket() {
    }

    public ProxyUpPacket(List<String> servers) {
        this.servers = servers;
    }

    @Override
    public void read(IPacketData data) {
        this.servers = new ArrayList<>();

        int size = Integer.parseInt(data.next());
        for (int i = 0; i < size; i++) {
            this.servers.add(data.next());
        }
    }

    public List<String> getServers() {
        return servers;
    }

    @Override
    public String write() {
        buildDefault().build(this.servers.size());
        this.servers.forEach(getBuilder()::build);
        return getBuilder().toString();
    }

}
