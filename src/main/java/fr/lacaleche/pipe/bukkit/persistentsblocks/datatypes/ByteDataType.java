package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces.DataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ByteDataType extends AbstractDataType<Byte> {

    public ByteDataType() {
        super(PersistentDataType.BYTE);
    }

    @Override
    public Byte get(PersistentDataContainer container, NamedKeys key) {
        return (Byte) container.get(key.getKey(), key.getType());
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, Byte value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a byte");

        container.set(key.getKey(), PersistentDataType.BYTE, value);
    }

}
