package fr.lacaleche.pipe.common.utils.server.interfaces;

import fr.lacaleche.pipe.common.utils.server.PipeServer;

import java.util.List;

public interface ServerManager {

    List<PipeServer> getServers();

    PipeServer getServer(String name);

    void registerServer(PipeServer server);

    void unregisterServer(String name);

}
