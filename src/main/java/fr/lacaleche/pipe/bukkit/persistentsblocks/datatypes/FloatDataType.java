package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FloatDataType extends AbstractDataType<Float> {

    public FloatDataType() {
        super(PersistentDataType.FLOAT);
    }

    @Override
    public Float get(PersistentDataContainer container, NamedKeys key) {
        return (Float) container.get(key.getKey(), key.getType());
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, Float value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a Float");

        container.set(key.getKey(), PersistentDataType.FLOAT, value);
    }

}
