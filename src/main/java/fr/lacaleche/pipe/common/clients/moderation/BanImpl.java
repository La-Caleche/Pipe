package fr.lacaleche.pipe.common.clients.moderation;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.clients.moderation.interfaces.IBan;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class BanImpl extends SqlModel implements IBan {

    @BelongsTo(column = "author_id")
    private ClientImpl author;
    @BelongsTo(column = "client_id")
    private ClientImpl client;
    @Property
    private String reason;
    @Property
    private LocalDateTime end_at;

    public BanImpl(ClientImpl author, ClientImpl client, String reason, LocalDateTime end_at) {
        this.author = author;
        this.client = client;
        this.reason = reason;
        this.end_at = end_at;
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

    @Override
    public LocalDateTime getEndAt() {
        return this.end_at;
    }

    @Override
    public void setEndAt(LocalDateTime end) {
        this.end_at = end;
        this.save();
    }

    @Override
    public boolean isActive() {
        return this.end_at == null || this.end_at.isAfter(LocalDateTime.now());
    }

    @Override
    public void unban(ClientImpl author) {
        this.setEndAt(LocalDateTime.now());
        this.save();
    }
}
