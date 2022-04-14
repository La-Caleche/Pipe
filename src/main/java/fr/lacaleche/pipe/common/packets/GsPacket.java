package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.Packet;

import java.util.UUID;

public class GsPacket extends Packet {
    
    private UUID   player;
    private String message;
    
    public GsPacket() {
    }
    
    public GsPacket(UUID player, String message) {
        this.player = player;
        this.message = message;
    }
    
    @Override
    public void read(String[] data) {
        this.player = UUID.fromString(data[1]);
        this.message = data[2];
    }
    
    public UUID getPlayer() {
        return player;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public String write() {
        String data = null;
        data = getBuilder().build(id()).build(player).build(message).toString();
        return data;
    }
    
}
