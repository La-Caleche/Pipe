package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import net.md_5.bungee.api.CommandSender;

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
    public void read(String[] data) {
        this.executor = CommandExecutor.Executor.valueOf(data[1]);
        if (this.executor == CommandExecutor.Executor.PLAYER) {
            this.sender = UUID.fromString(data[2]);
        } else {
            this.sender = null;
        }
        this.message = data[3];
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
        String data = null;
        data = getBuilder().build(id()).build(executor.name()).build(sender).build(message).toString();
        return data;
    }
    
}
