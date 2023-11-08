package fr.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.SqlDatabase;
import fr.lacaleche.core.databases.mysql.SqlDatabaseImpl;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.databases.mysql.morph.queries.CountQuery;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.TranslationImpl;
import fr.lacaleche.pipe.common.i18n.TranslationKeyImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class TestSqlBuilder {

    public static void main(String[] args) throws IOException, SQLException {
        TestSqlBuilder test = new TestSqlBuilder();
        test.start();
        test.run();
        test.stop();
    }

    private void run() {
        Core core = Core.get();
        Locale locale = Pipe.getCommon().getDefaultLocale();

        List<TranslationImpl> translations = new ModelFilter<TranslationImpl>().model(TranslationImpl.class).getAll().toList();
        int countInDb = core.<SqlDatabase>getDatabase().getDatastore().count(TranslationImpl.class).count();

        List<TranslationImpl> translationsWithoutLocale = translations.stream().filter(translation -> translation.getLocale() == null).findAny().stream().toList();

        Logger.info("Found %d/%d translations", translations.size(), countInDb);
        Logger.info("Found %d translations without locale", translationsWithoutLocale.size());

        Client client = new ModelFilter<ClientImpl>().model(ClientImpl.class).sql(sql -> sql.where("uuid", "973576cf-90b0-4137-8fcd-00e41fa2f562")).getOne();
        Logger.info(client.getUUID().toString());
        Logger.info("Slug: " + client.getLocale().getSlug() + ", id: " + client.getRank().getId());
        Logger.info(client.getRank().getSlug());

//        Locale locale = new ModelFilter<LocaleImpl>().model(LocaleImpl.class).sql(sql -> sql.where("slug", "fr")).getOne();
//        Logger.info(locale.getSlug());

//        client.setLocale(locale);
    }

    private void start() {
        Core core = Core.get();
        core.setDatabase(new SqlDatabaseImpl());

        if (!core.<SqlDatabase>getDatabase().openConnection()) {
            this.stop();
            throw new RuntimeException("Could not connect to database");
        }
    }

    private void stop() {
        Core core = Core.get();
        core.<SqlDatabase>getDatabase().closeConnection(true);
        core.getModelManager().flushCache();
    }
}
