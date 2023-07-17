package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ShortDataType extends AbstractDataType<Short> {

    public ShortDataType() {
        super(PersistentDataType.SHORT);
    }

    @Override
    public Short get(PersistentDataContainer container, NamedKeys key) {
        return (Short) container.get(key.getKey(), key.getType());
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, Short value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a Short");

        container.set(key.getKey(), PersistentDataType.SHORT, value);
    }

}
