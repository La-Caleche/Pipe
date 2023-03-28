package fr.lacaleche.pipe.common.clients.moderation;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.clients.moderation.interfaces.IKick;

import java.util.Date;

public class KickImpl extends SqlModel implements IKick {
    @BelongsTo(column = "author_id")
    private ClientImpl author;
    @BelongsTo(column = "client_id")
    private ClientImpl client;
    @Property
    private String reason;

    public KickImpl(ClientImpl author, ClientImpl client, String reason) {
        this.author = author;
        this.client = client;
        this.reason = reason;
        this.insertOrSave();
        this.cache();
    }
    @Override
    public ClientImpl getAuthor() {
        return this.author;
    }

    @Override
    public void setAuthor(ClientImpl client) {
        this.author = client;
        this.save();
    }

    @Override
    public ClientImpl getClient() {
        return this.client;
    }

    @Override
    public void setClient(ClientImpl client) {
        this.client = client;
        this.save();
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
        this.save();
    }
}
