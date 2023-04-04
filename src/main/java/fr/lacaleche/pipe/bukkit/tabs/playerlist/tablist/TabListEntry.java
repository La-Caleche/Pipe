package fr.lacaleche.pipe.bukkit.tabs.playerlist.tablist;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public class TabListEntry {

    private UUID uniqueId;
    private String name;
    private boolean listed;
    private int gameMode;
    private Component displayName;

    public TabListEntry(UUID uniqueId, String name, boolean listed, int gameMode, Component displayName) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.listed = listed;
        this.gameMode = gameMode;
        this.displayName = displayName;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public boolean isListed() {
        return listed;
    }

    public int getGameMode() {
        return gameMode;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public static class Builder {

        private final UUID uniqueId;
        private String name;
        private boolean listed;
        private int gameMode;
        private Component displayName;

        public Builder(UUID uniqueId) {
            this.uniqueId = uniqueId;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder listed(boolean listed) {
            this.listed = listed;
            return this;
        }

        public Builder gameMode(int gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        public Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public TabListEntry build() {
            return new TabListEntry(uniqueId, name, listed, gameMode, displayName);
        }
    }

}
