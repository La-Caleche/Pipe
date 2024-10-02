package fr.lacaleche.pipe.common.models.client.moderation;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.models.clients.AbstractCoreClient;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.models.client.moderation.interfaces.IBan;

import java.time.LocalDateTime;

public class BanImpl extends SqlModel implements IBan {

    @BelongsTo(column = "author_id")
    private AbstractCoreClient author;
    @BelongsTo(column = "unban_author_id")
    private AbstractCoreClient unbanAuthor;

    @BelongsTo(column = "client_id")
    private AbstractCoreClient client;
    @Property
    private String reason;
    @Property
    private LocalDateTime endAt;

    public BanImpl(AbstractCoreClient author, AbstractCoreClient client, String reason, LocalDateTime endAt) {
        this.author = author;
        this.client = client;
        this.reason = reason;
        this.endAt = endAt;
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
    public AbstractCoreClient getUnbanAuthor() {
        return unbanAuthor;
    }

    @Override
    public void setUnbanAuthor(AbstractCoreClient unbanAuthor) {
        this.unbanAuthor = unbanAuthor;
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

    @Override
    public LocalDateTime getEndAt() {
        return this.endAt;
    }

    @Override
    public void setEndAt(LocalDateTime end) {
        this.endAt = end;
        this.save();
    }

    @Override
    public boolean isActive() {
        return this.endAt == null || this.endAt.isAfter(LocalDateTime.now());
    }

    @Override
    public void unban(AbstractCoreClient author) {
        Logger.info("Unbanning %s", this.client.getUsername());
        this.waitBeforeSave();
        this.setEndAt(LocalDateTime.now());
        this.setUnbanAuthor(author);
        this.save(true);
    }
}
