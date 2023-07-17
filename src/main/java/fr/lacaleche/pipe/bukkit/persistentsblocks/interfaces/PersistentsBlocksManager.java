package fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces;

import fr.lacaleche.pipe.bukkit.persistentsblocks.enums.NamedKeys;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;

public interface PersistentsBlocksManager {

    PersistentDataContainer createContainer(Block block);

}
