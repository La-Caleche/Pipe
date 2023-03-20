package fr.lacaleche.pipe.common.utils.server;

import java.util.List;

public interface PipeServer {

    String getApp();

    String getHost();

    String getServerIcon();

    boolean isDevServer();

    int getMaxPlayers();

    int onlinePlayers();

    void setOnlinePlayers(int onlinePlayers);

    boolean isOnline();

    void setOnline(boolean online);

}
