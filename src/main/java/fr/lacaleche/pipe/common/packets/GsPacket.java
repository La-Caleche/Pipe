package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;

import java.util.UUID;

@Packet(name = "GsPacket")
public class GsPacket extends PacketImpl {

    private CommandExecutor.Executor executor;
    private UUID sender;
    private String message;
    
    public GsPacket() {
    }
    
    public GsPacket(CommandExecutor.Executor executor, UUID sender, String message) {
        this.executor = executor;
        this.sender = sender;
        this.message = message;
    }
    
    @Override
    public void read(IPacketData data) {
        this.executor = CommandExecutor.Executor.valueOf(data.next());
        if (this.executor == CommandExecutor.Executor.PLAYER) {
            this.sender = UUID.fromString(data.next());
        } else {
            this.sender = null;
        }
        this.message = data.next();
    }
    
    public UUID getSender() {
        return sender;
    }

    public CommandExecutor.Executor getExecutor() {
        return executor;
    }

    public String getMessage() {
        return message;
    }
    
    @Override
    public String write() {
        return buildDefault().build(this.executor.name()).build(this.sender).build(this.message).toString();
    }
    
}
