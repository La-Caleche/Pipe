package fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers;

import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheTextDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class HologramController extends LivingController {

    public HologramController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
    }

    @Override
    public void spawn() {
        IStorage st = this.getStorage();

        this.setEntity(new HologramController.CalecheHologram(st.handle(st.cast(StorageClass.CRAFT_WORLD, location.getWorld()))));
    }

    public void text(Component title) {
        IChatBaseComponent vanillaComponent = this.getStorage().construct(StorageConstructor.ADVENTURE_COMPONENT_CONSTRUCTOR, title);
        this.hologram().setText(vanillaComponent);
    }

    public void scale(Vector3f scale) {
        com.mojang.math.Transformation nms = CalecheDisplay.createTransformation(this.hologram().aj());
        Transformation transformation = new Transformation(nms.d(), nms.e(), nms.f(), nms.g());
        transformation.getScale().set(scale.x(), scale.y(), scale.z());
        this.hologram().setTransformation(new com.mojang.math.Transformation(transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation()));
    }

    public void setBillboard(CalecheDisplay.BillboardConstraints constraints) {
        this.hologram().setBillboardConstraints(constraints);
    }

    protected CalecheHologram hologram() {
        return this.getEntity();
    }

    public static class CalecheHologram extends CalecheTextDisplay {

        public CalecheHologram(World world) {
            super(EntityTypes.aX, world);
        }

    }

}
