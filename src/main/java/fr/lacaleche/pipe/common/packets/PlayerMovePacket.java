package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.Packet;

import java.util.UUID;

public class PlayerMovePacket extends Packet {

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
