package fr.lacaleche.pipe.proxy.modules.command;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.listeners.HelpPacketListener;
import fr.lacaleche.pipe.common.commands.listeners.RegisterNetworkCommandPacketListener;
import fr.lacaleche.pipe.common.packets.FetchNetworkCommandsPacket;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import fr.lacaleche.pipe.common.packets.RegisterNetworkCommandPacket;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;
import fr.lacaleche.pipe.proxy.modules.command.listeners.CommandListener;

@AModule(target = ModuleTarget.BUNGEE)
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
        ProxyPipeListenerManager manager = Pipe.get().getListenerManager();
        manager.registerProxyListener(this, commandListener);
        manager.registerCustomListener(this, commandListener);
        manager.registerCustomListener(this, new HelpPacketListener());
        manager.registerCustomListener(this, new RegisterNetworkCommandPacketListener());
    }

    @Override
    public void registerPackets() {
        CalecheCore.get().getPacketManager().registerPacket(HelpPacket.class);
        CalecheCore.get().getPacketManager().registerPacket(RegisterNetworkCommandPacket.class);
        CalecheCore.get().getPacketManager().registerPacket(FetchNetworkCommandsPacket.class);
    }

}
