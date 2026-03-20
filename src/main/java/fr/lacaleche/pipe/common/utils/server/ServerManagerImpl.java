package fr.lacaleche.pipe.common.utils.server;

import fr.lacaleche.pipe.common.utils.server.interfaces.ServerManager;

import java.util.ArrayList;
import java.util.List;

public class ServerManagerImpl implements ServerManager {

    private final List<PipeServer> servers;

    public ServerManagerImpl() {
        this.servers = new ArrayList<>();
    }

    @Override
    public List<PipeServer> getServers() {
        return servers;
    }

    @Override
    public PipeServer getServer(String host) {
        return servers.stream().filter(server -> server.getHost().equals(host)).findFirst().orElse(null);
    }

    @Override
    public void registerServer(PipeServer server) {
        servers.add(server);
    }

    @Override
    public void unregisterServer(String host) {
        servers.removeIf(server -> server.getHost().equals(host));
    }

}
