package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class IntegerDataType extends AbstractDataType<Integer> {

    public IntegerDataType() {
        super(PersistentDataType.INTEGER);
    }

    @Override
    public Integer get(PersistentDataContainer container, NamedKeys key) {
        return (Integer) container.get(key.getKey(), key.getType());
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, Integer value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a Integer");

        container.set(key.getKey(), PersistentDataType.INTEGER, value);
    }

}
