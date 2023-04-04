package fr.lacaleche.pipe.bukkit.tabs.features;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.tabs.features.interfaces.TabFeature;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist.TabListEntry;
import net.kyori.adventure.text.Component;

public abstract class AbstractTabFeature implements TabFeature {

    private TabManager tab;

    public AbstractTabFeature() {
        this.tab = Pipe.get().getTabManager();
    }

    protected TabListEntry getTabListInfo(TabPlayer tabPlayer, TabPlayer viewer) {
        Component component = tabPlayer.getTabList().getTabFormat(viewer.getClient().getLocale());

        return new TabListEntry(
                tabPlayer.getUniqueId(),
                tabPlayer.getName(),
                true,
                tabPlayer.getGameMode(),
                component
        );
    }

    protected TabManager tab() {
        return this.tab;
    }

    protected IStorage storage() {
        return this.tab().getNmsManager().getStorage();
    }

}
