package fr.lacaleche.pipe.common.models.i18n;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.models.i18n.LocaleImpl;
import fr.lacaleche.core.models.i18n.TranslationImpl;
import fr.lacaleche.core.models.i18n.TranslationKeyImpl;
import fr.lacaleche.core.models.i18n.interfaces.Locale;
import fr.lacaleche.core.models.i18n.interfaces.Translation;
import fr.lacaleche.core.models.i18n.interfaces.TranslationKey;

import java.util.List;

@AModule(target = ModuleTarget.ALL)
public class I18nModule extends Module {

    @Override
    public void onEnable() {
        List<TranslationKeyImpl> keys = new ModelFilter<TranslationKeyImpl>().model(TranslationKeyImpl.class).saveInCache().getAll().toList();
        Logger.debug("%d translation keys cached from database...", keys.size());

        List<LocaleImpl> locales = new ModelFilter<LocaleImpl>().model((Class<LocaleImpl>) this.getLocaleClassMatching()).saveInCache().getAll().toList();
        Logger.debug("%d locales cached from database...", locales.size());

        Core.get().getCommandManager().getCaptionProvider().loadLocales();
    }

    @Override
    public void onDisable() {
        List<LocaleImpl> cachedLocales = Core.get().getModelManager().get((Class<LocaleImpl>) this.getLocaleClassMatching()).stream().toList();
        List<TranslationImpl> cachedTranslations = Core.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys = Core.get().getModelManager().get(TranslationKeyImpl.class).stream().toList();

        Logger.debug("Found %d locales in cache...".formatted(cachedLocales.size()));
        Logger.debug("Found %d translations in cache...".formatted(cachedTranslations.size()));
        Logger.debug("Found %d translation keys in cache...".formatted(cachedTranslationKeys.size()));

        Logger.debug("Expiring %d translations...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.debug("Expiring %d translation keys...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        Logger.debug("Expiring %d locales...".formatted(cachedLocales.size()));
        cachedLocales.forEach(Locale::expireNow);

        Core.get().getCommandManager().getCaptionProvider().clear();
    }

    @Override
    public void onReload() {
        List<LocaleImpl> cachedLocales = Core.get().getModelManager().get((Class<LocaleImpl>) this.getLocaleClassMatching()).stream().toList();
        List<TranslationImpl> cachedTranslations = Core.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys = Core.get().getModelManager().get(TranslationKeyImpl.class).stream().toList();

        Logger.debug("Found %d locales in cache...".formatted(cachedLocales.size()));
        Logger.debug("Found %d translations in cache...".formatted(cachedTranslations.size()));
        Logger.debug("Found %d translation keys in cache...".formatted(cachedTranslationKeys.size()));

        Logger.debug("Expiring %d translations...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.debug("Expiring %d translation keys...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        List<TranslationKeyImpl> keys = new ModelFilter<TranslationKeyImpl>().model(TranslationKeyImpl.class).saveInCache().getAll().toList();
        Logger.debug("%d translation keys cached from database...", keys.size());

        Logger.debug("Refreshing %d locales...".formatted(cachedLocales.size()));
        cachedLocales.forEach(Locale::refresh);
    }

    private Class<? extends LocaleImpl> getLocaleClassMatching() {
        return Core.get().getClassMatcher().match(LocaleImpl.class);
    }

}
