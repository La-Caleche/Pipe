package fr.lacaleche.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.redis.JedisFactory;

import java.io.IOException;
import java.sql.SQLException;

public class TestRedis {

    public static void main(String[] args) throws IOException, SQLException {
        TestRedis test = new TestRedis();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        Core.get().getJedisFactory().getPacketManager().publish("test", "proxy");
    }

    private void start() {
        final Core core = Core.get();
        core.setDatabase(new SqlDatabaseImpl());
        core.registerJedisFactory(new JedisFactory());

        if (core.<SqlDatabase>getDatabase().openConnection()) {
            Logger.info("Mysql connection initialized");
        } else {
            Logger.error("Mysql connection failed");
        }
    }

    private void stop() {
        Core.get().getJedisFactory().close();

        if (Core.get().<SqlDatabase>getDatabase().closeConnection(true)) {
            Logger.info("Mysql connection closed");
        } else {
            Logger.error("Mysql connection failed");
        }
    }
}
