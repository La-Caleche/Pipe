package fr.lacaleche.pipe.common.models.client.moderation;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.models.clients.AbstractCoreClient;
import fr.lacaleche.pipe.common.models.client.moderation.interfaces.IKick;

public class KickImpl extends SqlModel implements IKick {

    @BelongsTo(column = "author_id")
    private AbstractCoreClient author;
    @BelongsTo(column = "client_id")
    private AbstractCoreClient client;
    @Property
    private String reason;

    public KickImpl(AbstractCoreClient author, AbstractCoreClient client, String reason) {
        this.author = author;
        this.client = client;
        this.reason = reason;
        this.insertOrSave();
    }
    @Override
    public AbstractCoreClient getAuthor() {
        return this.author;
    }

    @Override
    public void setAuthor(AbstractCoreClient client) {
        this.author = client;
        this.save();
    }

    @Override
    public AbstractCoreClient getClient() {
        return this.client;
    }

    @Override
    public void setClient(AbstractCoreClient client) {
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
