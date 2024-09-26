package fr.lacaleche.pipe.common.models.client.moderation.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.models.clients.AbstractCoreClient;

@Entity("kicks")
public interface IKick {
    AbstractCoreClient getAuthor();

    void setAuthor(AbstractCoreClient client);

    AbstractCoreClient getClient();

    void setClient(AbstractCoreClient client);

    String getReason();

    void setReason(String reason);
}
