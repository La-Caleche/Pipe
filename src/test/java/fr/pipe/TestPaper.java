package fr.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.io.IOException;
import java.sql.SQLException;

public class TestPaper {

    public static void main(String[] args) throws IOException, SQLException {
        TestPaper test = new TestPaper();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        
    }

    private void start() {
        Core core = Core.get();
        core.setDatabase(new SqlDatabaseImpl());

        if (core.<SqlDatabase>getDatabase().openConnection()) {
            Logger.info("Mysql connection initialized");
        } else {
            Logger.err("Mysql connection failed");
        }
    }

    private void stop() {
        if (Core.get().<SqlDatabase>getDatabase().closeConnection(true)) {
            Logger.info("Mysql connection closed");
        } else {
            Logger.err("Mysql connection failed");
        }
    }
}
