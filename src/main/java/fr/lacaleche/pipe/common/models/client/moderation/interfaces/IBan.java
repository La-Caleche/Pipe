package fr.lacaleche.pipe.common.models.client.moderation.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.models.clients.AbstractCoreClient;

import java.time.LocalDateTime;

@Entity("bans")
public interface IBan {

    AbstractCoreClient getAuthor();

    void setAuthor(AbstractCoreClient client);

    AbstractCoreClient getUnbanAuthor();

    void setUnbanAuthor(AbstractCoreClient client);

    AbstractCoreClient getClient();

    void setClient(AbstractCoreClient client);

    String getReason();

    void setReason(String reason);

    LocalDateTime getEndAt();

    void setEndAt(LocalDateTime end);

    boolean isActive();

    void unban(AbstractCoreClient author);
}
