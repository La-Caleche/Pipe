package fr.lacaleche.pipe.proxy.commands;

import fr.lacaleche.pipe.proxy.commands.interfaces.NetworkCommand;

public class NetworkCommandImpl implements NetworkCommand {

    private String app;
    private String host;
    private String label;

        public NetworkCommandImpl(String app, String host, String label) {
        this.app = app;
        this.host = host;
        this.label = label;
    }

    @Override
    public String app() {
        return this.app;
    }

    @Override
    public String host() {
        return this.host;
    }

    @Override
    public String label() {
        return this.label;
    }
}
