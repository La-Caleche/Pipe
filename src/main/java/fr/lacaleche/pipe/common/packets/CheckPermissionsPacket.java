package fr.lacaleche.pipe.common.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.PacketImpl;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.core.utils.serializer.interfaces.CoreSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Packet(name = "CheckPermissionsPacket")
public class CheckPermissionsPacket extends TransactionalPacket {

    private List<String> commands;
    private UUID player;

    public CheckPermissionsPacket() {
    }

    public CheckPermissionsPacket(UUID player, Token token) {
        this.player = player;

        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public CheckPermissionsPacket(List<String> commands, UUID player, Resolve<Object> resolve, Reject<Object> reject) {
        this.commands = commands;
        this.player = player;

        ObjectNode commandsNode = new ObjectMapper().createObjectNode();
        ArrayNode array = commandsNode.putArray("commands");

        this.commands.forEach(command -> {
            array.add(CoreSerializer.get().serialize(new AllowedCommand(command)).getJsonNode());
        });

        this.setResponse(commandsNode.toString());
        this.setPacketType(PacketType.REQUEST);
        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
    }

    @Override
    public void read(IPacketData data) {
        this.setToken(new Token(data.next()));
        this.setPacketType(PacketType.valueOf(data.next()));

        this.player = UUID.fromString(data.next());

        try {
            this.setResponse(this.parseJson(new ObjectMapper().readValue(data.<String>next(), JsonNode.class)));
        } catch (JsonProcessingException e) {
            SentryAPIImpl.getInstance().captureException(e);
        }

        if (this.getPacketType() == PacketType.ANSWER && data.hasNext()) {
            this.setResult(TransactionResult.valueOf(data.next()));
        }
    }

    public UUID getPlayer() {
        return player;
    }

    private List<AllowedCommand> parseJson(JsonNode jsonNode) {
        List<AllowedCommand> commands = new ArrayList();
        ArrayNode arrayNode = jsonNode.withArray("commands");
        arrayNode.forEach(elem -> {
            AllowedCommand allowedCommand = new AllowedCommand(elem.get("command").asText());
            allowedCommand.setAllowed(elem.get("allowed").asBoolean());
            commands.add(allowedCommand);
        });
        return commands;
    }

    @Override
    public String write() {
        if (this.getPacketType() == PacketType.REQUEST) {
            CalecheCore.get().getTransactionManager().registerTransaction(new Transaction(this, this.getToken(), this.getResolve(), this.getReject()));
        }

        buildDefault().build(this.player.toString()).build(this.getResponse());

        if (this.getPacketType() == PacketType.ANSWER) {
            getBuilder().build(this.getResult());
        }

        return getBuilder().toString();
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
