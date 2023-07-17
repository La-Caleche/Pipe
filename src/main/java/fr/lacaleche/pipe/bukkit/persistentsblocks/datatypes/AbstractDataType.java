package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces.DataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class AbstractDataType<T> implements DataType<T> {

    protected final PersistentDataType<?, ?> type;

    public AbstractDataType(PersistentDataType<?, ?> type) {
        this.type = type;
    }

    @Override
    public boolean valid(PersistentDataContainer container, NamedKeys key) {
        return key.getType() == this.type;
    }

    @Override
    public boolean has(PersistentDataContainer container, NamedKeys key) {
        return this.valid(container, key) && container.has(key.getKey(), key.getType());
    }

    @Override
    public T getOrDefault(PersistentDataContainer container, NamedKeys key, T defaultValue) {
        return has(container, key) ? get(container, key) : defaultValue;
    }
}
