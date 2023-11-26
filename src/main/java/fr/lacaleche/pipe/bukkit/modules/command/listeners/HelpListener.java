package fr.lacaleche.pipe.bukkit.modules.command.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.events.interfaces.CoreListener;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.events.PacketReadEvent;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.redis.reader.PacketReader;
import fr.lacaleche.core.utils.seripet.interfaces.CoreSerializer;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.commands.helper.command.HelperImpl;
import fr.lacaleche.pipe.common.commands.helper.interfaces.Helper;
import fr.lacaleche.pipe.common.packets.CheckPermissionsPacket;
import fr.lacaleche.pipe.common.packets.HelpPacket;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class HelpListener implements CoreListener {

    @PacketReader(packet = HelpPacket.class)
    public void onHelpPacket(PacketReadEvent event, HelpPacket packet) {
        if (packet.getPacketType() == PacketType.REQUEST && packet.getHost().equals(Core.get().getHost())) {
            if (Pipe.getBukkit().getCommandManager().isRegistered(packet.getCommand())) {
                Plugin plugin = Pipe.getBukkit().getPlugin();
                Player player = plugin.getServer().getPlayer(packet.getPlayer());
                if (player == null) return;

                TextComponent.Builder formatted = new HelperImpl(packet.getLocale(), packet.getCommand()).format(player);

                HelpPacket responsePacket = new HelpPacket(packet.getHost(), packet.getPlayer(), packet.getCommand(), packet.getLocale(), packet.getToken());
                responsePacket.setResult(TransactionResult.ACCEPT);
                responsePacket.setResponse(CoreSerializer.get().toJson(GsonComponentSerializer.gson().serialize(formatted.asComponent())));

                Core.get().getPacketManager().publish(responsePacket, event.from());
            }
        }
    }

    @PacketReader(packet = CheckPermissionsPacket.class)
    public void onCheckPermissionsPacket(PacketReadEvent event, CheckPermissionsPacket packet) {
        if (packet.getPacketType() == PacketType.REQUEST) {
            Pipe.getBukkit().getTaskManager().newTask(taskBuilder -> taskBuilder.run(task -> {
                Plugin plugin = Pipe.getBukkit().getPlugin();
                Player player = plugin.getServer().getPlayer(packet.getPlayer());
                Client client = Pipe.getBukkit().getClient(packet.getPlayer());
                if (player == null || client == null) return;

                List<CheckPermissionsPacket.AllowedCommand> commands = packet.getResponseAsList(CheckPermissionsPacket.AllowedCommand.class);
                CheckPermissionsPacket responsePacket = new CheckPermissionsPacket(packet.getPlayer(), packet.getToken());
                responsePacket.setResult(TransactionResult.ACCEPT);
                responsePacket.setResponse(commands.stream().peek(command -> {
                    String label = command.getCommand().replace("∅", "");
                    if (Pipe.getBukkit().getCommandManager().isRegistered(label)) {
                        Helper helper = new HelperImpl(client.getLocale(), label);
                        command.setAllowed(helper.senderCanUseCommand(player));
                    }
                }).toList());
                Core.get().getPacketManager().publish(responsePacket, event.from());
            }).startAfter(10));
        }
    }

}
