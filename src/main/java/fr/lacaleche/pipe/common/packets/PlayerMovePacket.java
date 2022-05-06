package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;

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
    public void read(String[] data) {
        this.player = UUID.fromString(data[1]);
        this.server = data[2];
    }

    public UUID getPlayer() {
        return player;
    }

    public String getServer() {
        return server;
    }

    @Override
    public String write() {
        String data = null;
        data = getBuilder().build(id()).build(player).build(server).toString();
        return data;
    }

}
