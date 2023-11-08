package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.util.UUID;

@Packet(name = "PlayerMovePacket")
@Serializer(variables = {"player", "server"})
public class PlayerMovePacket extends PacketImpl {

    private UUID             player;
    private String           server;

    public PlayerMovePacket() {
    }

    public PlayerMovePacket(UUID player, String server) {
        this.player = player;
        this.server = server;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getServer() {
        return server;
    }

}
