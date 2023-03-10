package fr.lacaleche.pipe.proxy.modules.command;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipe.proxy.modules.command.listeners.CommandListeners;

@AModule(target = ModuleTarget.PROXY)
public class CommandModule extends Module {

    public CommandModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        CommandListeners commandListener = new CommandListeners();
        ProxyPipeListenerManager manager = Pipe.get().getListenerManager();
        manager.registerProxyListener(this, commandListener);
        manager.registerCustomListener(this, commandListener);
    }

    @Override
    public void registerPackets() {
        CalecheCore.get().getPacketManager().registerPacket(HelpPacket.class);
    }

}
