package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;

import java.util.UUID;

@Packet(name = "GsPacket")
@Serializer(variables = {"executor", "sender", "message", "server", "proxy"})
public class GsPacket extends PacketImpl {

    private CommandExecutor.Executor executor;
    private UUID sender;
    private String message;
    private String server;
    private String proxy;
    
    public GsPacket() {
    }
    
    public GsPacket(CommandExecutor.Executor executor, String message) {
        this.executor = executor;
        this.message = message;
        this.sender = null;
        this.proxy = Core.get().getHost();
        this.server = "";
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public String getProxy() {
        return proxy;
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
    
}
