package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "RegisterNewServerPacket")
public class RegisterNewServerPacket extends PacketImpl {

    private String app;
    private String host;
    private List<String> commands;

    public RegisterNewServerPacket() {
    }

    public RegisterNewServerPacket(String app, String host, List<String> commands) {
        this.app = app;
        this.host = host;
        this.commands = commands;
    }

    @Override
    public void read(IPacketData data) {
        this.commands = new ArrayList<>();

        this.app = data.next();
        this.host = data.next();
        int size = Integer.parseInt(data.next());
        for (int i = 0; i < size; i++) {
            this.commands.add(data.next());
        }
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getApp() {
        return app;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String write() {
        buildDefault().build(this.app).build(this.host).build(this.commands.size());
        this.commands.forEach(getBuilder()::build);
        return getBuilder().toString();
    }

}
