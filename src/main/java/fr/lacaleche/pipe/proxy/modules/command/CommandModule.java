package fr.lacaleche.pipe.proxy.modules.command;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipe.proxy.modules.command.listeners.CommandListener;
import fr.lacaleche.pipe.proxy.utils.ProxyListenersUtils;

public class CommandModule extends Module {

    public CommandModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void registerListeners() {
        CommandListener commandListener = new CommandListener();
        ProxyPipeListenerManager manager = (ProxyPipeListenerManager) Pipe.get().getListenerManager();
        manager.registerProxyListener(this, commandListener);
        manager.registerCustomListener(this, commandListener);
    }

}
