package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.util.UUID;

@Packet(name = "HelpPacket")
@Serializer(variables = {"host", "player", "command", "locale"})
public class HelpPacket extends TransactionalPacket {

    private UUID player;
    private String host;
    private String command;
    private LocaleImpl locale;

    public HelpPacket() {
    }

    public HelpPacket(String host, UUID player, String command, Locale locale, Token token) {
        this.host = host;
        this.player = player;
        this.command = command;
        this.locale = (LocaleImpl) locale;

        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public HelpPacket(String host, UUID player, String command, Locale locale, Resolve<Object> resolve, Reject<Object> reject) {
        this.host = host;
        this.player = player;
        this.command = command;
        this.locale = (LocaleImpl) locale;

        this.setPacketType(PacketType.REQUEST);
        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
    }

    public String getCommand() {
        return command;
    }

    public LocaleImpl getLocale() {
        return locale;
    }

    public String getHost() {
        return host;
    }

    public UUID getPlayer() {
        return player;
    }

}
