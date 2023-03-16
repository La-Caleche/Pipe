package fr.lacaleche.pipe.common.tabs.impl;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.common.tabs.interfaces.TabManager;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.TablistFormatManager;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import me.neznamy.tab.api.team.UnlimitedNametagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class TabManagerImpl implements TabManager {

    private TabAPI tabAPI;

    public TabManagerImpl() {
        this.tabAPI = TabAPI.getInstance();
    }

    @Override
    public TabAPI getTabAPI() {
        return tabAPI;
    }

    @Override
    public UnlimitedNametagManager getUNM() {
        return (UnlimitedNametagManager) this.tabAPI.getTeamManager();
    }

    @Override
    public TablistFormatManager getTFM() {
        return this.tabAPI.getTablistFormatManager();
    }

    @Override
    public void setName(TabPlayer tabPlayer, Component component) {
        if (this.tabAPI == null) return;
        this.getUNM().setName(tabPlayer, serialize(component));
    }

    @Override
    public void setTabName(TabPlayer tabPlayer, Component component) {
        if (this.tabAPI == null) return;
        this.getTFM().setName(tabPlayer, serialize(component));
    }

    @Override
    public void setTabPrefix(TabPlayer tabPlayer, Component component) {
        if (this.tabAPI == null) return;
        this.getTFM().setPrefix(tabPlayer, serialize(component));
    }

    @Override
    public void setTabSuffix(TabPlayer tabPlayer, Component component) {
        if (this.tabAPI == null) return;
        this.getTFM().setSuffix(tabPlayer, serialize(component));
    }

    private String serialize(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

}
