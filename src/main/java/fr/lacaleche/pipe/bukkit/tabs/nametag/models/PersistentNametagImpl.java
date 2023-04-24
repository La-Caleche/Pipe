package fr.lacaleche.pipe.bukkit.tabs.nametag.models;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.BelongsTo;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;

public class PersistentNametagImpl extends SqlModel implements PersistentNametag {

    @BelongsTo(column = "client_id")
    private final ClientImpl client;

    @Property
    private int indexOrder;

    @Property
    private String rawText;

    public PersistentNametagImpl(ClientImpl client, int indexOrder, String rawText) {
        this.client = client;
        this.indexOrder = indexOrder;
        this.rawText = rawText;

        this.insertOrSave();
        this.cache();
    }

    @Override
    public Client getClient() {
        return this.client;
    }

    @Override
    public int getIndexOrder() {
        return this.indexOrder;
    }

    @Override
    public String getRawText() {
        return this.rawText;
    }

    @Override
    public void setIndexOrder(int indexOrder) {
        this.indexOrder = indexOrder;
        this.save();
    }

    @Override
    public void setRawText(String rawText) {
        this.rawText = rawText;
        this.save();
    }
}
