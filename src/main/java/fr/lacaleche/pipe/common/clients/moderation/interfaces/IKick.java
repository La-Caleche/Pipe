package fr.lacaleche.pipe.common.clients.moderation.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.pipe.common.clients.ClientImpl;

import java.util.Date;

@Entity("kicks")
public interface IKick {
    ClientImpl getAuthor();

    void setAuthor(ClientImpl client);

    ClientImpl getClient();

    void setClient(ClientImpl client);

    String getReason();

    void setReason(String reason);
}
