package fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist;

import com.mojang.authlib.GameProfile;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.tabs.playerlist.interfaces.TabListPlayer;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabManager;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.bukkit.tabs.scoreboard.TabScoreboard;
import fr.lacaleche.pipe.bukkit.tabs.scoreboard.interfaces.Scoreboard;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import net.kyori.adventure.text.Component;

import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageFields.*;

import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageClass.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageConstructor.*;
import static fr.lacaleche.pipe.bukkit.tabs.nms.enums.TabStorageFields.*;

import java.util.*;

public class TabListPlayerImpl implements TabListPlayer {

    private final TabManager tab;
    private final TabPlayer tabPlayer;

    private String format;

    public TabListPlayerImpl(TabManager tab, TabPlayer tabPlayer) {
        this.tab = tab;
        this.tabPlayer = tabPlayer;

        this.format = "<rank> <player>";
    }

    @Override
    public TabPlayer getTabPlayer() {
        return tabPlayer;
    }

    @Override
    public void addTabListPlayers(List<TabListEntry> entries) {
        Class<Enum> actionsClazz = (Class<Enum>) this.storage().clazz(PCB_PLAYER_INFO_ACTION);
        this.createPacket(entries, EnumSet.allOf(actionsClazz));
    }

    @Override
    public void updateGamemodes(Map<UUID, Integer> entries) {
        List<TabListEntry> tabListEntries = entries.entrySet().stream().map(entry ->
                new TabListEntry.Builder(entry.getKey()).gameMode(entry.getValue()).build()).toList();
        Class<Enum> actionsClazz = (Class<Enum>) this.storage().clazz(PCB_PLAYER_INFO_ACTION);
        this.createPacket(tabListEntries, EnumSet.of(Enum.valueOf(actionsClazz, "UPDATE_GAME_MODE")));
    }

    @Override
    public void updateDisplayNames(Map<UUID, Component> entries) {
        List<TabListEntry> tabListEntries = entries.entrySet().stream().map(entry ->
                new TabListEntry.Builder(entry.getKey()).displayName(entry.getValue()).build()).toList();
        Class<Enum> actionsClazz = (Class<Enum>) this.storage().clazz(PCB_PLAYER_INFO_ACTION);
        this.createPacket(tabListEntries, EnumSet.of(Enum.valueOf(actionsClazz, "UPDATE_DISPLAY_NAME")));
    }

    private void createPacket(List<TabListEntry> entries, EnumSet<?> actions) {
        List<Object> players = new ArrayList<>();
        entries.forEach(entry -> {
            GameProfile profile = new GameProfile(entry.getUniqueId(), entry.getName());

            players.add(this.storage().construct(
                    PCB_PLAYER_INFO_DATA_CONSTRUCTOR,
                    entry.getUniqueId(),
                    profile,
                    entry.isListed(),
                    0,
                    this.tab.int2GameMode(entry.getGameMode()),
                    this.storage().construct(ADVENTURE_COMPONENT_CONSTRUCTOR, entry.getDisplayName()),
                    null
            ));
        });

        Object packet = this.storage().construct(PACKET_CLIENTBOUND_PLAYER_INFO_UPDATE_CONSTRUCTOR, actions, Collections.emptyList());
        this.storage().set(PCB_PLAYER_INFO_UPDATE$PLAYERS, packet, players);

        this.tab.getNmsManager().sendPacket(this.getTabPlayer().getPlayer(), packet);
    }

    @Override
    public Component getTabFormat(Locale locale) {
        return Pipe.get().text().deserialize(this.format, locale, this.getTabPlayer().getPlaceHolders());
    }

    @Override
    public void setPlayerListFormat(String format) {
        this.format = format;
    }

    private IStorage storage() {
        return this.tab.getNmsManager().getStorage();
    }

}
