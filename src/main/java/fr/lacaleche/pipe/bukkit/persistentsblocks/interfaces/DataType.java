package fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public interface DataType<T> {

    boolean valid(PersistentDataContainer container, NamedKeys key);

    boolean has(PersistentDataContainer container, NamedKeys key);

    T get(PersistentDataContainer container, NamedKeys key);

    T getOrDefault(PersistentDataContainer container, NamedKeys key, T defaultValue);

    void set(PersistentDataContainer container, NamedKeys key, T value);

}
