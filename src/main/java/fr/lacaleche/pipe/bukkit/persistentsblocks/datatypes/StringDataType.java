package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class StringDataType extends AbstractDataType<String> {

    public StringDataType() {
        super(PersistentDataType.STRING);
    }

    @Override
    public String get(PersistentDataContainer container, NamedKeys key) {
        return (String) container.get(key.getKey(), key.getType());
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, String value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a String");

        container.set(key.getKey(), PersistentDataType.STRING, value);
    }

}
