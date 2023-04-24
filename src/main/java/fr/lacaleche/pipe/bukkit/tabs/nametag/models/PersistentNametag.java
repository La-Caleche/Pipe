package fr.lacaleche.pipe.bukkit.tabs.nametag.models;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.interfaces.ISqlModel;
import fr.lacaleche.pipe.common.clients.Client;

@Entity("persistent_nametags")
public interface PersistentNametag extends ISqlModel {

    Client getClient();

    int getIndexOrder();

    String getRawText();

    void setIndexOrder(int indexOrder);

    void setRawText(String rawText);

}
