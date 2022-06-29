package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

@Packet(name = "RegisterNetworkCommandPacket")
public class RegisterNetworkCommandPacket extends PacketImpl {

    private String from;
    private String command;

    public RegisterNetworkCommandPacket() {
    }

    public RegisterNetworkCommandPacket(String from, String command) {
        this.from = from;
        this.command = command;
    }

    @Override
    public void read(IPacketData data) {
        this.from = data.next();
        this.command = data.next();
    }

    public String getFrom() {
        return from;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String write() {
        return buildDefault().build(this.from).build(this.command).toString();
    }

}
