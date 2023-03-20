package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;

import java.util.ArrayList;
import java.util.List;

@Packet(name = "RegisterNewServerPacket")
public class RegisterServerPacket extends PacketImpl {

    private String app;
    private String host;
    private String serverIcon;
    private int maxPlayers;
    private List<String> commands;

    public RegisterServerPacket() {
    }

    public RegisterServerPacket(String app, String host, String serverIcon, int maxPlayers, List<String> commands) {
        this.app = app;
        this.host = host;
        this.serverIcon = serverIcon;
        this.maxPlayers = maxPlayers;
        this.commands = commands;
    }

    @Override
    public void read(IPacketData data) {
        this.commands = new ArrayList<>();

        this.app = data.next();
        this.host = data.next();
        this.serverIcon = data.next();
        this.maxPlayers = Integer.parseInt(data.next());
        int size = Integer.parseInt(data.next());
        for (int i = 0; i < size; i++) {
            this.commands.add(data.next());
        }
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getApp() {
        return app;
    }

    public String getServerIcon() {
        return serverIcon;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String write() {
        buildDefault().build(this.app).build(this.host).build(this.serverIcon).build(this.maxPlayers).build(this.commands.size());
        this.commands.forEach(getBuilder()::build);
        return getBuilder().toString();
    }

}
