package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;
import fr.lacaleche.core.utils.seripet.interfaces.CoreSerializer;

import java.util.List;

@Packet(name = "ProxyUpPacket")
@Serializer(variables = {"proxyHost", "servers"})
public class ProxyUpPacket extends PacketImpl {

    private String proxyHost;
    private List<String> servers;

    public ProxyUpPacket() {
    }

    public ProxyUpPacket(String host, List<String> servers) {
        this.proxyHost = host;
        this.servers = servers;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public List<String> getServers() {
        return servers;
    }

}
