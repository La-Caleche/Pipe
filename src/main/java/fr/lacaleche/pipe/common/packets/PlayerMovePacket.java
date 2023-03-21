package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.UUID;

@Packet(name = "PlayerMovePacket")
public class PlayerMovePacket extends PacketImpl {

    private UUID             player;
    private String           server;

    public PlayerMovePacket() {
    }

    public PlayerMovePacket(UUID player, String server) {
        this.player = player;
        this.server = server;
    }
    
    @Override
    public void read(IPacketData data) {
        this.player = UUID.fromString(data.next());
        this.server = data.next();
    }

    public UUID getPlayer() {
        return player;
    }

    public String getServer() {
        return server;
    }

    @Override
    public String write() {
        return buildDefault().build(this.player).build(this.server).toString();
    }

}
