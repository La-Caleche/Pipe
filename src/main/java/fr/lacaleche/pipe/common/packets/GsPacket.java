package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.Core;
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

    @Override
    public void read(IPacketData data) {
        this.executor = CommandExecutor.Executor.valueOf(data.next());
        if (this.executor == CommandExecutor.Executor.PLAYER) {
            this.sender = UUID.fromString(data.next());
        } else {
            data.next();
            this.sender = null;
        }
        this.proxy = data.next();
        this.server = data.next();
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
        return buildDefault().build(this.executor.name()).build(this.sender).build(this.proxy).build(this.server).build(this.message).toString();
    }
    
}
