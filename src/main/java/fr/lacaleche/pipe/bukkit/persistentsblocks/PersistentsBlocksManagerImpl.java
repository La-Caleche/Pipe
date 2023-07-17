package fr.lacaleche.pipe.bukkit.persistentsblocks;

import com.jeff_media.customblockdata.CustomBlockData;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.persistentsblocks.interfaces.PersistentsBlocksManager;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;

public class PersistentsBlocksManagerImpl implements PersistentsBlocksManager {

    @Override
    public PersistentDataContainer createContainer(Block block) {
        return new CustomBlockData(block, Pipe.getBukkit().getPlugin());
    }

}
