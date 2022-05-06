package fr.lacaleche.pipe.common.modules.i18n;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
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

public class I18nModule extends Module {

    public I18nModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void onEnable() {
        List<LocaleImpl> cachedLocales = CalecheCore.get().getModelManager().get(LocaleImpl.class).stream().toList();
        List<TranslationImpl> cachedTranslations = CalecheCore.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys = CalecheCore.get().getModelManager().get(TranslationKeyImpl.class).stream().toList();

        Logger.customDebug("Found %d locales in cache...".formatted(cachedLocales.size()));
        Logger.customDebug("Found %d translations in cache...".formatted(cachedTranslations.size()));
        Logger.customDebug("Found %d translation keys in cache...".formatted(cachedTranslationKeys.size()));

        Logger.customDebug("Expiring %d translations...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.customDebug("Expiring %d translation keys...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        this.loadDefaults();

        Logger.customDebug("Refreshing %d locales...".formatted(cachedLocales.size()));
        cachedLocales.forEach(Locale::refresh);
    }

    @Override
    public void onDisable() {
        List<LocaleImpl> cachedLocales = CalecheCore.get().getModelManager().get(LocaleImpl.class).stream().toList();
        List<TranslationImpl> cachedTranslations = CalecheCore.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys = CalecheCore.get().getModelManager().get(TranslationKeyImpl.class).stream().toList();

        Logger.customDebug("Found %d locales in cache...".formatted(cachedLocales.size()));
        Logger.customDebug("Found %d translations in cache...".formatted(cachedTranslations.size()));
        Logger.customDebug("Found %d translation keys in cache...".formatted(cachedTranslationKeys.size()));

        Logger.customDebug("Removing %d translations from cache...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.customDebug("Removing %d translation keys from cache...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        Logger.customDebug("Removing %d locales from cache...".formatted(cachedLocales.size()));
        cachedLocales.forEach(Locale::expireNow);
    }

    @Override
    public void onReload() {
        List<ClientImpl> clients = CalecheCore.get().getModelManager().get(ClientImpl.class).stream().toList();
        List<Locale> toKeep = new ArrayList<>(clients.stream().map(Client::getLocale).toList());
        List<LocaleImpl> cachedLocales = CalecheCore.get().getModelManager().get(LocaleImpl.class).stream().toList();
        List<TranslationImpl> cachedTranslations = CalecheCore.get().getModelManager().get(TranslationImpl.class).stream().toList();
        List<TranslationKeyImpl> cachedTranslationKeys;
        List<Locale> toRemove = new ArrayList<>(cachedLocales);
        toRemove.removeAll(toKeep);

        cachedTranslations = cachedTranslations.stream().filter(translation -> !toRemove.contains(translation.getLocale())).toList();
        cachedTranslationKeys = cachedTranslations.stream().map(Translation::getKey).toList();

        Logger.customDebug("Removing %d translations from cache...".formatted(cachedTranslations.size()));
        cachedTranslations.forEach(Translation::expireNow);

        Logger.customDebug("Removing %d translation keys from cache...".formatted(cachedTranslationKeys.size()));
        cachedTranslationKeys.forEach(TranslationKey::expireNow);

        Logger.customDebug("Removing %d locales from cache...".formatted(toRemove.size()));
        toRemove.forEach(Locale::expireNow);

        Logger.customDebug("Refreshing %d locales...".formatted(toKeep.size()));
        toKeep.forEach(Locale::refresh);

        this.loadDefaults();
    }

    private void loadDefaults() {
        List<TranslationKeyImpl> keys = new ModelFilter<TranslationKeyImpl>().list(TranslationKeyImpl.class, true).toList();
        Logger.customDebug("Found %d translation keys in database...".formatted(keys.size()));
    }

}
