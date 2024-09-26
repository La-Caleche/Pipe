package fr.lacaleche.pipe.common.models.client.interfaces;

import fr.lacaleche.core.models.clients.CoreClient;
import fr.lacaleche.pipe.common.models.client.moderation.BanImpl;
import fr.lacaleche.pipe.common.models.client.moderation.KickImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public interface PipeClient<T> extends CoreClient<PipeLocale, PipeRank> {

    boolean isOnline();

    T getPlayer();

    BanImpl getLastBan();

    void kick(PipeClient<T> author, String reason, BiConsumer<Boolean, Optional<Exception>> callback);

    void ban(PipeClient<T> author, String reason, LocalDateTime endAt, BiConsumer<Boolean, Optional<Exception>> callback);

    void unban(PipeClient<T> author, BiConsumer<Boolean, Optional<Exception>> callback);

    List<BanImpl> getBans();

    List<KickImpl> getKicks();

}
