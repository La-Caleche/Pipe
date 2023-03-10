package fr.lacaleche.pipe.common.packets;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.utils.Token;
import fr.lacaleche.core.utils.promises.interfaces.Reject;
import fr.lacaleche.core.utils.promises.interfaces.Resolve;
import fr.lacaleche.core.utils.redis.packet.TransactionalPacket;
import fr.lacaleche.core.utils.redis.packet.annotations.Packet;
import fr.lacaleche.core.utils.redis.packet.enums.PacketType;
import fr.lacaleche.core.utils.redis.packet.interfaces.IPacketData;
import fr.lacaleche.core.utils.redis.packet.transaction.Transaction;
import fr.lacaleche.core.utils.redis.packet.transaction.enums.TransactionResult;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.util.UUID;

@Packet(name = "HelpPacket")
public class HelpPacket extends TransactionalPacket {

    private UUID player;
    private String host;
    private String command;
    private Locale locale;

    public HelpPacket() {
    }

    public HelpPacket(String host, UUID player, String command, Locale locale, Token token) {
        this.host = host;
        this.player = player;
        this.command = command;
        this.locale = locale;

        this.setToken(token);
        this.setPacketType(PacketType.ANSWER);
    }

    public HelpPacket(String host, UUID player, String command, Locale locale, Resolve<Object> resolve, Reject<Object> reject) {
        this.host = host;
        this.player = player;
        this.command = command;
        this.locale = locale;

        this.setResponse("{}");
        this.setPacketType(PacketType.REQUEST);
        this.setToken(new Token(64));
        this.setResolve(resolve);
        this.setReject(reject);
    }

    @Override
    public void read(IPacketData data) {
        this.setToken(new Token(data.next()));
        this.setPacketType(PacketType.valueOf(data.next()));

        this.host = data.next();
        this.player = UUID.fromString(data.next());
        this.command = data.next();
        String localeSlug = data.next();
        this.locale = new ModelFilter<LocaleImpl>().find(LocaleImpl.class, model -> model.getSlug().equals(localeSlug));
        this.setResponse(data.next());

        if (this.getPacketType() == PacketType.ANSWER && data.hasNext()) {
            this.setResult(TransactionResult.valueOf(data.next()));
        }
    }

    public String getCommand() {
        return command;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getHost() {
        return host;
    }

    public UUID getPlayer() {
        return player;
    }

    @Override
    public String write() {
        if (this.getPacketType() == PacketType.REQUEST) {
            CalecheCore.get().getTransactionManager().registerTransaction(new Transaction(this, this.getToken(), this.getResolve(), this.getReject()));
        }

        buildDefault().build(this.host).build(this.player).build(this.command).build(this.locale.getSlug()).build(this.getResponse());

        if (this.getPacketType() == PacketType.ANSWER) {
            getBuilder().build(this.getResult());
        }

        return getBuilder().toString();
    }

}
