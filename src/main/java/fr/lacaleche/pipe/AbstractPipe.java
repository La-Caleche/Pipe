package fr.lacaleche.pipe;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.databases.mysql.morph.builder.sql.Where;
import fr.lacaleche.core.models.clients.ranks.AbstractCoreRank;
import fr.lacaleche.core.models.clients.ranks.interfaces.CoreRank;
import fr.lacaleche.core.models.i18n.LocaleImpl;
import fr.lacaleche.core.models.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.adventure.PipeTextImpl;
import fr.lacaleche.pipe.common.models.client.PipeLocaleImpl;
import fr.lacaleche.pipe.common.models.client.PipeRankImpl;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeClient;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class AbstractPipe implements Pipe {

    private static Pipe instance;

    private PipeLocale defaultLocale;
    private final PipeText pipeText;

    public AbstractPipe() {
        instance = this;
        this.pipeText = new PipeTextImpl();

        Core.get().getClassMatcher().registerMatch(CoreRank.class, PipeRankImpl.class)
                .registerMatch(AbstractCoreRank.class, PipeRankImpl.class)
                .registerMatch(Locale.class, PipeLocaleImpl.class)
                .registerMatch(LocaleImpl.class, PipeLocaleImpl.class);
    }

    public static Pipe get() {
        if (instance == null) throw new IllegalStateException("Pipe instance is null");
        return instance;
    }

    @Override
    public PipeText text() {
        return this.pipeText;
    }

    @Override
    public PipeLocale getDefaultLocale() {
        if (this.defaultLocale == null)
            this.defaultLocale = new ModelFilter<PipeLocaleImpl>()
                    .model(PipeLocaleImpl.class)
                    .sql(sql -> sql.where(new Where("is_default", true)))
                    .cache(Locale::isDefault).getOne();
        return this.defaultLocale;
    }

    @Override
    public @NonNull PipeLocale getLocale(PipeClient<?> recipient) {
        final PipeClient<?> client = (PipeClient<?>) Core.get().getClient(recipient);
        return client == null ? this.getDefaultLocale() : client.getLocale();
    }

}
