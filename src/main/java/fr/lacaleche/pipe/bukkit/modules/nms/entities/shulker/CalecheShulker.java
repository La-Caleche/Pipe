package fr.lacaleche.pipe.bukkit.modules.nms.entities.shulker;

import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheEntity;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityShulker;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

import java.lang.reflect.InvocationTargetException;

public class CalecheShulker extends CalecheEntity {

    public CalecheShulker(NMSManager nmsManager, Location location) {
        super(nmsManager, nmsManager.getStorage(ShulkerStorage.class), location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new EntityShulker(EntityTypes.ay, this.getStorage().handle(this.getStorage().cast(StorageClass.CRAFT_WORLD, location.getWorld()))));

        shulker().j(true); // setInvisible
        shulker().i(true); // setGlowing
    }

    private EntityShulker shulker() {
        return (EntityShulker) this.getEntity();
    }

}
