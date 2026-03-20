package fr.lacaleche.pipe.common.utils.server;

import fr.lacaleche.core.utils.seripet.annotations.Serializer;
import fr.lacaleche.pipe.common.packets.RegisterServerPacket;

@Serializer(as = PipeServerImpl.class, variables = {"app", "host", "serverIcon", "maxPlayers", "onlinePlayers", "online", "devServer"})
public class PipeServerImpl implements PipeServer {

    private final String app;
    private final String host;
    private final String serverIcon;
    private final int maxPlayers;
    private int onlinePlayers;
    private boolean online;
    private final boolean devServer;

    public PipeServerImpl(RegisterServerPacket packet) {
        this(packet.getApp(), packet.getHost(), packet.getServerIcon(), packet.getMaxPlayers(), packet.isDevServer());
    }

    public PipeServerImpl(String app, String host, String serverIcon, int maxPlayers, boolean devServer) {
        this.app = app;
        this.host = host;
        this.serverIcon = serverIcon;
        this.maxPlayers = maxPlayers;
        this.devServer = devServer;

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

    @Override
    public boolean isDevServer() {
        return this.devServer;
    }
}
