package fr.lacaleche.pipe.bukkit.tabs.nametag;

import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheLiving;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheTextDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.ArmorStandController;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageClass;
import fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageConstructor;
import fr.lacaleche.pipe.bukkit.modules.nms.impls.LivingController;
import fr.lacaleche.pipe.bukkit.modules.nms.interfaces.IStorage;
import fr.lacaleche.pipe.bukkit.tabs.interfaces.TabPlayer;
import fr.lacaleche.pipe.common.adventure.PipeText;
import fr.lacaleche.pipe.common.clients.Client;
import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.DATA_WATCHER$SET;
import static fr.lacaleche.pipe.bukkit.modules.nms.enums.StorageMethods.GET_DATA_WATCHER;

public class NameTagController extends ArmorStandController {

    private TabPlayer tabPlayer;
    private int order;
    private String rawText;

    private boolean toRemove;

    public NameTagController(NMSManager nmsManager, Location location) {
        super(nmsManager, location);
        this.toRemove = false;

        this.setNoTick(true);
    }

    public int getOrder() {
        return order;
    }

    public void setTabPlayer(TabPlayer tabPlayer) {
        this.tabPlayer = tabPlayer;
        this.location.setY(this.getYOffset(this.tabPlayer.getPlayer().getLocation().getY()));
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void teleport() {
        Player player = this.tabPlayer.getPlayer();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        this.teleport(x, this.getYOffset(y), z);
    }

    public void setText(String rawText) {
        this.rawText = rawText;

        this.updateText();
    }

    public void updateText() {
        this.setTitle(Pipe.getBukkit().text().deserialize(this.rawText, this.getViewer().getClient().getLocale(), this.tabPlayer.getPlaceHolders()));

        this.enqueueUpdateMetadata();
    }

    public void setForRemoval() {
        this.toRemove = true;
    }

    public boolean needRemove() {
        return this.toRemove;
    }

    public void setSneak(boolean sneak) {
        this.setShiftKeyDown(sneak);
    }


    public String getRawText() {
        return this.rawText;
    }

    public TabPlayer getViewer() {
        Optional<Player> player = this.getViewers().stream().findFirst();
        return player.map(value -> Pipe.getBukkit().getTabManager().getTabPlayer(value.getUniqueId())).orElse(null);
    }

    @Override
    public void spawn() {
        super.spawn();
        this.setMarker(true);
    }

    private double getYOffset(double originalY) {
        double playerY = 1.8;
        if (this.tabPlayer.getPlayer().isSneaking()) playerY = 1.37;
        else if (this.tabPlayer.getPlayer().isSleeping()) playerY = 0.2;

        int[] orders = this.tabPlayer.getNameTag().getLines().keySet().stream().mapToInt(Integer::intValue).sorted().toArray();
        int index = Arrays.binarySearch(orders, this.order);

        return originalY + playerY + index * 0.26;
    }

}
