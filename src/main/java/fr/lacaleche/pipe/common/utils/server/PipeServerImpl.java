package fr.lacaleche.pipe.common.utils.server;

import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.pipe.common.packets.RegisterServerPacket;

@Serializer(variables = {"app", "host", "serverIcon", "maxPlayers", "onlinePlayers", "online"})
public class PipeServerImpl implements PipeServer {

    private String app;
    private String host;
    private String serverIcon;
    private int maxPlayers;
    private int onlinePlayers;
    private boolean online;

    public PipeServerImpl(RegisterServerPacket packet) {
        this(packet.getApp(), packet.getHost(), packet.getServerIcon(), packet.getMaxPlayers());
    }

    public PipeServerImpl(String app, String host, String serverIcon, int maxPlayers) {
        this.app = app;
        this.host = host;
        this.serverIcon = serverIcon;
        this.maxPlayers = maxPlayers;

        this.onlinePlayers = 0;
        this.online = false;
    }

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getServerIcon() {
        return serverIcon;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public int onlinePlayers() {
        return this.onlinePlayers;
    }

    @Override
    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }
}
