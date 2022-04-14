package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;

import java.util.UUID;

public class ConnectPacket extends Packet {

    private UUID player;
    private String server;
    private Token token;
    private Resolve<Object> resolve;
    private Reject<Object> reject;
    private PacketType packetType;
    private boolean result;

    public ConnectPacket() {
    }

    public ConnectPacket(UUID player, String server, Token token, boolean result) {
        this.player = player;
        this.server = server;
        this.token = token;
        this.result = result;
        this.packetType = PacketType.ANSWER;
    }

    public ConnectPacket(UUID player, String server, Resolve<Object> resolve, Reject<Object> reject) {
        this.player = player;
        this.server = server;
        this.token = new Token(64);
        this.resolve = resolve;
        this.reject = reject;
        this.packetType = PacketType.REQUEST;
    }
    
    @Override
    public void read(String[] data) {
        this.packetType = PacketType.valueOf(data[1]);
        this.player = UUID.fromString(data[2]);
        this.server = data[3];
        this.token = new Token(data[4]);
        this.result = Boolean.parseBoolean(data[5]);
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getServer() {
        return server;
    }

    public Token getToken() {
        return token;
    }

    public boolean getResult() {
        return result;
    }

    @Override
    public String write() {
        String data = null;
        data = getBuilder().build(id()).build(getPacketType()).build(player).build(server).build(token).build(result).toString();

        if (getPacketType() == PacketType.REQUEST) {
            CalecheCore.get().getTransactionManager().registerTransaction(new Transaction(this, token, resolve, reject));
        }

        return data;
    }

}
