package fr.lacaleche.pipe.common.logs;

import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;

public class LogsImpl extends SqlModel implements ILogs {

    @Property
    private String source;
    @Property
    private String data;

    public LogsImpl(String source, String data) {
        this.source = source;
        this.data = data;
        this.save();
        this.insert();
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
        this.save();
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
        this.save();
    }
}
