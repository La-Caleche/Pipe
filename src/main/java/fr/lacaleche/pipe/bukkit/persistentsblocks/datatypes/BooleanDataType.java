package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BooleanDataType extends AbstractDataType<Boolean> {

    public BooleanDataType() {
        super(PersistentDataType.BYTE);
    }

    @Override
    public Boolean get(PersistentDataContainer container, NamedKeys key) {
        byte byteValue = (Byte) container.get(key.getKey(), key.getType());
        return byteValue == 1;
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, Boolean value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a Boolean");

        container.set(key.getKey(), PersistentDataType.BYTE, value ? (byte) 1 : (byte) 0);
    }

}
