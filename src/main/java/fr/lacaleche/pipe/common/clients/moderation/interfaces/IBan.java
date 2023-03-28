package fr.lacaleche.pipe.common.clients.moderation.interfaces;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;

import java.util.Date;

@Entity("bans")
public interface IBan {

    ClientImpl getAuthor();
    void setAuthor(ClientImpl client);

    ClientImpl getClient();
    void setClient(ClientImpl client);

    String getReason();
    void setReason(String reason);

    Date getEndAt();
    void setEndAt(Date end);


    boolean isActive();

    void unban(ClientImpl author);
}
