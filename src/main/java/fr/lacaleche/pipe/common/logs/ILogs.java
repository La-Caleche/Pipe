package fr.lacaleche.pipe.common.logs;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;

@Entity("logs")
public interface ILogs {

    String getSource();
    void setSource(String source);

    String getData();
    void setData(String data);
}
