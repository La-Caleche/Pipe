package fr.lacaleche.pipe.bukkit.modules.server.listeners;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.command.CommandModule;
import fr.lacaleche.pipe.bukkit.modules.server.ServerModule;
import fr.lacaleche.pipe.common.commands.interfaces.CommandManager;
import fr.lacaleche.pipe.common.packets.ProxyUpPacket;
import fr.lacaleche.pipe.common.packets.RegisterNewServerPacket;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class ProxyPacketsListener implements CoreListener {

    @PacketReader(packet = ProxyUpPacket.class)
    public void onHelpPacket(PacketReadEvent event, ProxyUpPacket packet) {
        ServerModule module = CalecheCore.get().getCentralModuleManager().getModule(ServerModule.class);
        module.registerServer();
    }

}
