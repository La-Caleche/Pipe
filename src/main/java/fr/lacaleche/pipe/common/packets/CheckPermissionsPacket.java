package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.util.List;
import java.util.UUID;

@Packet(name = "CheckPermissionsPacket")
@Serializer(variables = {"commands", "player"})
public class CheckPermissionsPacket extends TransactionalPacket {

    private List<String> commands;
    private UUID player;

    public CheckPermissionsPacket() {
        super();
    }

    public CheckPermissionsPacket(UUID player, Token token) {
        super();
        this.player = player;

        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public CheckPermissionsPacket(List<String> commands, UUID player, Resolve<Object> resolve, Reject<Object> reject) {
        super();
        this.commands = commands;
        this.player = player;

        this.setResponse(this.commands.stream().map(AllowedCommand::new).toList());
        this.setPacketType(PacketType.REQUEST);
        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
    }

    public UUID getPlayer() {
        return player;
    }

    @Serializer(variables = {"command", "allowed"})
    public static class AllowedCommand {

        private final String command;
        private boolean allowed = false;

        public AllowedCommand(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }

        public boolean isAllowed() {
            return allowed;
        }

        public void setAllowed(boolean allowed) {
            this.allowed = allowed;
        }

    }

}
