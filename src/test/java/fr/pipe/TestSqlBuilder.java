package fr.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.TranslationImpl;
import fr.lacaleche.pipe.common.i18n.TranslationKeyImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TestSqlBuilder {

    public static void main(String[] args) throws IOException, SQLException {
        TestSqlBuilder test = new TestSqlBuilder();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        List<TranslationImpl> translations = new ModelFilter<TranslationImpl>().model(TranslationImpl.class).getAll().toList();




//        Client client = new ModelFilter<ClientImpl>().model(ClientImpl.class).sql(sql -> sql.where("uuid", "973576cf-90b0-4137-8fcd-00e41fa2f562")).getOne();
//        Logger.info(client.getUUID().toString());
//        Logger.info("Slug: " + client.getLocale().getSlug() + ", id: " + client.getRank().getId());
//        Logger.info(client.getRank().getSlug());
//
//        Locale locale = new ModelFilter<LocaleImpl>().model(LocaleImpl.class).sql(sql -> sql.where("slug", "fr")).getOne();
//        Logger.info(locale.getSlug());
//
//        client.setLocale(locale);
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
