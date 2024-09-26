package fr.lacaleche.pipe.common.models.client;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.models.annotations.HasMany;
import fr.lacaleche.core.models.clients.AbstractCoreClient;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeClient;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeLocale;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeRank;
import fr.lacaleche.pipe.common.models.client.moderation.BanImpl;
import fr.lacaleche.pipe.common.models.client.moderation.KickImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class PipeAbstractClient<C> extends AbstractCoreClient<PipeLocale, PipeRank> implements PipeClient<C> {

    @HasMany(clazz = BanImpl.class, field = "client_id")
    private List<BanImpl> bans;

    @HasMany(clazz = KickImpl.class, field = "author_id")
    private List<KickImpl> kicks;

    protected C cachedPlayer;

    public PipeAbstractClient(UUID uuid, String username) {
        super(uuid, username);
        this.save();
    }

    @Override
    public boolean expire() {
        if (this.isOnline()) {
            Logger.info("Trying to expire a client that is still online. Username: %s, UUID: %s", this.getUsername(), this.getUUID().toString());
            return false;
        }
        this.cachedPlayer = null;
        return super.expire();
    }

    @Override
    public BanImpl getLastBan() {
        return this.bans.stream().filter(BanImpl::isActive).findFirst().orElse(null);
    }

    @Override
    public void kick(PipeClient<C> author, String reason, BiConsumer<Boolean, Optional<Exception>> callback) {
        Core.get().getTaskManager().newTask(builder -> builder.run(task -> {
            new KickImpl((AbstractCoreClient) author, this, reason);
            this.refresh();
            callback.accept(true, Optional.empty());
        }).error(exception -> {
            SentryAPIImpl.getInstance().captureException(exception);
            callback.accept(false, Optional.of(exception));
        }).async(true).executeNow(true));
    }

    @Override
    public void ban(PipeClient<C> author, String reason, LocalDateTime endAt, BiConsumer<Boolean, Optional<Exception>> callback) {
        Core.get().getTaskManager().newTask(builder -> builder.run(task -> {
            new BanImpl((AbstractCoreClient) author, this, reason, endAt);
            this.refresh();
            callback.accept(true, Optional.empty());
        }).error(exception -> {
            SentryAPIImpl.getInstance().captureException(exception);
            callback.accept(false, Optional.of(exception));
        }).async(true).executeNow(true));
    }

    @Override
    public void unban(PipeClient<C> author, BiConsumer<Boolean, Optional<Exception>> callback) {
        Core.get().getTaskManager().newTask(builder -> builder.run(task -> {
            this.bans.stream().filter(BanImpl::isActive).findFirst().ifPresent(ban -> ban.unban((AbstractCoreClient) author));
            callback.accept(true, Optional.empty());
        }).error(exception -> {
            SentryAPIImpl.getInstance().captureException(exception);
            callback.accept(false, Optional.of(exception));
        }).async(true).executeNow(true));
    }

    @Override
    public List<BanImpl> getBans() {
        return this.bans;
    }

    @Override
    public List<KickImpl> getKicks() {
        return this.kicks;
    }

    @Override
    public PipeRank getRank() {
        return (PipeRank) this.rank;
    }

    @Override
    public PipeLocale getLocale() {
        return (PipeLocale) this.locale;
    }
}
