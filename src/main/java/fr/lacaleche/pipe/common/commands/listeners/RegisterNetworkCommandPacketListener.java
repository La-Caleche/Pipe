package fr.lacaleche.pipe.common.commands.listeners;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.packets.FetchNetworkCommandsPacket;
import fr.lacaleche.pipe.common.packets.RegisterNetworkCommandPacket;

public class RegisterNetworkCommandPacketListener implements CoreListener {

    @PacketReader(packet = RegisterNetworkCommandPacket.class)
    public void onShareCommandListPacket(PacketReadEvent event, RegisterNetworkCommandPacket packet) {
        CommandManager manager = Pipe.get().getCommandManager();
        manager.addNetworkCommand(packet.getFrom(), packet.getCommand());
    }

    @PacketReader(packet = FetchNetworkCommandsPacket.class)
    public void onFetchNetworkCommandPacket(PacketReadEvent event, FetchNetworkCommandsPacket packet) {
        CommandManager manager = Pipe.get().getCommandManager();

        manager.getCommands().keySet().forEach((label) -> {
            RegisterNetworkCommandPacket commandPacket = new RegisterNetworkCommandPacket(CalecheCore.get().getAppName(), label);
            CalecheCore.get().getPacketManager().publish(commandPacket);
        });

        manager.getAliases().keySet().forEach((alias) -> {
            RegisterNetworkCommandPacket commandPacket = new RegisterNetworkCommandPacket(CalecheCore.get().getAppName(), "∅" + alias);
            CalecheCore.get().getPacketManager().publish(commandPacket);
        });
    }
    
}
