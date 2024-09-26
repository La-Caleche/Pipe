package fr.lacaleche.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.utils.logger.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class TestsWithDb {

    public static void main(String[] args) throws IOException, SQLException {
        TestsWithDb test = new TestsWithDb();
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
