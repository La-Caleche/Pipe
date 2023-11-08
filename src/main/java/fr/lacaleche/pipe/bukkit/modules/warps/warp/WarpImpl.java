package fr.lacaleche.pipe.bukkit.modules.warps.warp;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.BukkitPipe;
import fr.lacaleche.pipe.bukkit.mysql.annotations.BukkitBlob;
import fr.lacaleche.pipe.bukkit.mysql.serializers.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WarpImpl extends SqlModel implements IWarp {

    @Property
    private String name;
    @Property
    private String host;
    @BukkitBlob(serializer = LocationSerializer.class)
    private Location location;

    @Property
    private String world;

    public WarpImpl(String name, Location location) {
        super();

        this.name = name;
        this.host = Core.get().getHost();
        this.location = location;
        this.world = location.getWorld().getName();

        this.insertOrSave();
        this.cache();
    }

    @Override
    public void loaded() {
        if (this.location.getWorld() == null || !this.location.getWorld().getName().equals(this.getWorld())) {
            BukkitPipe pipe = Pipe.getBukkit();
            World thisWorld = pipe.getPlugin().getServer().getWorld(this.getWorld());
            World locWorld = this.location.getWorld();
            if (thisWorld == null && locWorld != null) this.world = locWorld.getName();
            else if (thisWorld != null && locWorld == null) this.location.setWorld(thisWorld);
            else if (thisWorld != null) this.location.setWorld(thisWorld);

            this.save();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.save();
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
        this.save();
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
        this.world = location.getWorld().getName();

        this.save();
    }

    @Override
    public String getWorld() {
        return world;
    }

    public void teleport(Player player) {
        player.teleport(this.location);
    }
}
