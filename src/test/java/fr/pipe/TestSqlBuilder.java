package fr.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.io.IOException;
import java.sql.SQLException;

public class TestSqlBuilder {

    public static void main(String[] args) throws IOException, SQLException {
        TestSqlBuilder test = new TestSqlBuilder();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        Client client = new ModelFilter<ClientImpl>().find(ClientImpl.class, null, sqlBuilder -> sqlBuilder.where("uuid", "973576cf-90b0-4137-8fcd-00e41fa2f562"));
        Logger.info(client.getUUID().toString());
        Logger.info("Slug: " + client.getLocale().getSlug() + ", id: " + client.getRank().getId());
        Logger.info(client.getRank().getSlug());

        Locale locale = new ModelFilter<LocaleImpl>().find(LocaleImpl.class, l -> l.getSlug().equals("fr"), sqlBuilder -> sqlBuilder.where("slug", "fr"));
        Logger.info(locale.getSlug());

        client.setLocale(locale);

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
