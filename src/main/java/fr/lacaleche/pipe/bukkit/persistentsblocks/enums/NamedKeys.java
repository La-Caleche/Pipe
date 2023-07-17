package fr.lacaleche.pipe.bukkit.persistentsblocks.enums;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import javax.inject.Named;

public enum NamedKeys {

    CBI("cbi", "key", PersistentDataType.BYTE),
    CBI_CATEGORY("cbi", "category", PersistentDataType.STRING),
    CBI_NAME("cbi", "name", PersistentDataType.STRING),
    CBI_TALL_INDEX("cbi", "tall_index", PersistentDataType.INTEGER),
    CBI_BLOCK_FACE("cbi", "block_face", PersistentDataType.STRING),
    PLATFORMS_ID("platform", "id", PersistentDataType.INTEGER);

    private NamespacedKey key;
    private PersistentDataType<?, ?> type;

    NamedKeys(String ns, String key, PersistentDataType<?, ?> type) {
        this.key = new NamespacedKey(ns, key);
        this.type = type;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public PersistentDataType<?, ?> getType() {
        return type;
    }

}
