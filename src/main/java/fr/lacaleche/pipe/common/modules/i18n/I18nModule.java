package fr.lacaleche.pipe.common.modules.i18n;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.clients.ClientImpl;
import fr.lacaleche.pipe.common.i18n.LocaleImpl;
import fr.lacaleche.pipe.common.i18n.TranslationImpl;
import fr.lacaleche.pipe.common.i18n.TranslationKeyImpl;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import fr.lacaleche.pipe.common.i18n.interfaces.Translation;
import fr.lacaleche.pipe.common.i18n.interfaces.TranslationKey;

import java.util.ArrayList;
import java.util.List;

@AModule(target = ModuleTarget.ALL)
public class I18nModule extends Module {

    public I18nModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        List<TranslationKeyImpl> keys = new ModelFilter<TranslationKeyImpl>().model(TranslationKeyImpl.class).saveInCache().getAll().toList();
        Logger.customDebug("%d translation keys cached from database...", keys.size());

        List<LocaleImpl> locales = new ModelFilter<LocaleImpl>().model(LocaleImpl.class).saveInCache().getAll().toList();
        Logger.customDebug("%d locales cached from database...", locales.size());
    }

    @Override
    public void onDisable() {
        List<LocaleImpl> cachedLocales = Core.get().getModelManager().get(LocaleImpl.class).stream().toList();
        List<TranslationImpl> cachedTranslations = Core.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys = Core.get().getModelManager().get(TranslationKeyImpl.class).stream().toList();

        Logger.customDebug("Found %d locales in cache...".formatted(cachedLocales.size()));
        Logger.customDebug("Found %d translations in cache...".formatted(cachedTranslations.size()));
        Logger.customDebug("Found %d translation keys in cache...".formatted(cachedTranslationKeys.size()));

        Logger.customDebug("Expiring %d translations...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.customDebug("Expiring %d translation keys...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        Logger.customDebug("Expiring %d locales...".formatted(cachedLocales.size()));
        cachedLocales.forEach(Locale::expireNow);
    }

    @Override
    public void onReload() {
        List<LocaleImpl> cachedLocales = Core.get().getModelManager().get(LocaleImpl.class).stream().toList();
        List<TranslationImpl> cachedTranslations = Core.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys = Core.get().getModelManager().get(TranslationKeyImpl.class).stream().toList();

        Logger.customDebug("Found %d locales in cache...".formatted(cachedLocales.size()));
        Logger.customDebug("Found %d translations in cache...".formatted(cachedTranslations.size()));
        Logger.customDebug("Found %d translation keys in cache...".formatted(cachedTranslationKeys.size()));

        Logger.customDebug("Expiring %d translations...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.customDebug("Expiring %d translation keys...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        List<TranslationKeyImpl> keys = new ModelFilter<TranslationKeyImpl>().model(TranslationKeyImpl.class).saveInCache().getAll().toList();
        Logger.customDebug("%d translation keys cached from database...", keys.size());

        Logger.customDebug("Refreshing %d locales...".formatted(cachedLocales.size()));
        cachedLocales.forEach(Locale::refresh);
    }

}
