package fr.lacaleche.pipe.common.tabs.interfaces;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.TablistFormatManager;
import me.neznamy.tab.api.team.UnlimitedNametagManager;
import net.kyori.adventure.text.Component;

public interface TabManager {

    TabAPI getTabAPI();

    UnlimitedNametagManager getUNM();

    TablistFormatManager getTFM();

    void setName(TabPlayer tabPlayer, Component component);

    void setTabName(TabPlayer tabPlayer, Component component);

    void setTabPrefix(TabPlayer tabPlayer, Component component);

    void setTabSuffix(TabPlayer tabPlayer, Component component);

}
