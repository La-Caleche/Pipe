package fr.lacaleche.pipe.bukkit.persistentsblocks.datatypes;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.block.BlockFace;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockFaceDataType extends AbstractDataType<BlockFace> {

    public BlockFaceDataType() {
        super(PersistentDataType.STRING);
    }

    @Override
    public BlockFace get(PersistentDataContainer container, NamedKeys key) {
        return BlockFace.valueOf((String) container.get(key.getKey(), key.getType()));
    }

    @Override
    public void set(PersistentDataContainer container, NamedKeys key, BlockFace value) {
        if (!this.valid(container, key))
            throw new IllegalArgumentException("The key type must be a String");

        container.set(key.getKey(), PersistentDataType.STRING, value.name());
    }

}
