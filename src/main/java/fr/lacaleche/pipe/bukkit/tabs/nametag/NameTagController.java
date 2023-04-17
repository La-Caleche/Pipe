package fr.lacaleche.pipe.bukkit.tabs.nametag;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheTextDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.ArmorStandController;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class NameTagController extends ArmorStandController {

    private TabPlayer tabPlayer;
    private int order;

    private boolean toRemove;

    public NameTagController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);

        this.toRemove = false;
    }

    public int getOrder() {
        return order;
    }

    public void setTabPlayer(TabPlayer tabPlayer) {
        this.tabPlayer = tabPlayer;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void refresh() {
        this.setInvisible(true);
        this.updateMetadata();

        Location newLocation = this.tabPlayer.getPlayer().getLocation().clone();
        newLocation.setY(this.getYOffset());
        this.teleport(newLocation);
    }

    public void remove() {
        this.toRemove = true;
    }

    public boolean needRemove() {
        return this.toRemove;
    }

    @Override
    public void spawn() {
        super.spawn();
//        this.setBillboard(CalecheDisplay.BillboardConstraints.CENTER);
        this.refresh();
    }

    private double getYOffset() {
        double playerEyes = this.tabPlayer.getPlayer().getLocation().getY() + 2;
        int linesNumber = this.tabPlayer.getNameTag().getLines().size();
        int index = this.order + 1;

        return playerEyes + (linesNumber - index) * 0.26;
    }

    protected CalecheNameTag nameTag() {
        return this.getEntity();
    }

    public static class CalecheNameTag extends ArmorStandController.CalecheArmorStand {

        public CalecheNameTag(World world) {
            super(world);
        }

    }

}
