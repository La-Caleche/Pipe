package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LongDataType extends AbstractDataType<Long> {

    public LongDataType() {
        super(PersistentDataType.LONG);
    }

    @Override
    public Long get(PersistentDataContainer container, NamedKeys key) {
        return (Long) container.get(key.getKey(), key.getType());
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, Long value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a Long");

        container.set(key.getKey(), PersistentDataType.LONG, value);
    }

}
