package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "ProxyUpPacket")
public class ProxyUpPacket extends PacketImpl {

    private String proxyHost;
    private List<String> servers;

    public ProxyUpPacket() {
    }

    public ProxyUpPacket(String host, List<String> servers) {
        this.proxyHost = host;
        this.servers = servers;
    }

    @Override
    public void read(IPacketData data) {
        this.servers = new ArrayList<>();

        this.proxyHost = data.next();

        int size = Integer.parseInt(data.next());
        for (int i = 0; i < size; i++) {
            this.servers.add(data.next());
        }
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public List<String> getServers() {
        return servers;
    }

    @Override
    public String write() {
        buildDefault().build(this.proxyHost).build(this.servers.size());
        this.servers.forEach(getBuilder()::build);
        return getBuilder().toString();
    }

}
