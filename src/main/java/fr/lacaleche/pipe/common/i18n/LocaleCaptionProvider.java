package fr.lacaleche.pipe.common.i18n;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.cloud.caption.*;

import java.util.*;

public class LocaleCaptionProvider<C> implements CaptionProvider<C> {

    public final List<String> keys;
    public final Map<Locale, ConstantCaptionProvider<C>> localedCaptionProviders;

    public LocaleCaptionProvider() {
        this.keys = new ArrayList<>();
        this.localedCaptionProviders = new HashMap<>();
    }

    public void clear() {
        this.keys.clear();
        this.localedCaptionProviders.clear();
    }

    public void loadLocales() {
        Core.get().getModelManager().get(LocaleImpl.class).forEach(locale -> {
            ImmutableConstantCaptionProvider.@NonNull Builder<C> builder = CaptionProvider.constantProvider();
            this.localedCaptionProviders.put(locale, builder.build());
        });
    }

    public void putCaption(Caption caption) {
        if (this.keys.contains(caption.key()))
            return;
        final Set<Locale> locales = this.localedCaptionProviders.keySet();
        locales.forEach(locale -> {
            ConstantCaptionProvider<C> provider = this.localedCaptionProviders.get(locale);
            ImmutableConstantCaptionProvider.@NonNull Builder<C> builder = CaptionProvider.constantProvider();
            builder.from(provider).putCaption(caption, locale.t(caption.key()).t());
            this.localedCaptionProviders.put(locale, builder.build());
        });
        this.keys.add(caption.key());
    }

    @Override
    public @Nullable String provide(@NonNull Caption caption, @NonNull C recipient) {
        return this.localedCaptionProviders.get(Pipe.get().getLocale(recipient)).provide(caption, recipient);
    }
}
