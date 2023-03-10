package fr.lacaleche.pipe.bukkit.modules.server;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.server.listeners.ProxyPacketsListener;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.packets.ProxyUpPacket;
import fr.lacaleche.pipe.common.packets.RegisterNewServerPacket;

import java.util.ArrayList;
import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class ServerModule extends BukkitModule {

    public ServerModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnableFinish() {
        this.registerServer();
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.get().getListenerManager();
        bukkitManager.registerCustomListener(this, new ProxyPacketsListener());
    }

    @Override
    public void registerPackets() {
        CalecheCore.get().getPacketManager().registerPacket(RegisterNewServerPacket.class);
        CalecheCore.get().getPacketManager().registerPacket(ProxyUpPacket.class);
    }

    public void registerServer() {
        CalecheCore core = CalecheCore.get();
        CommandManager manager = Pipe.get().getCommandManager();

        List<String> commands = new ArrayList<>(manager.getCommands().keySet());
        commands.addAll(manager.getAliases().keySet().stream().map((alias) -> "∅" + alias).toList());

        RegisterNewServerPacket registerNewServerPacket = new RegisterNewServerPacket(core.getAppName(), core.getHost(), commands);
        core.getJedisFactory().getPacketManager().publish(registerNewServerPacket);
    }

}
