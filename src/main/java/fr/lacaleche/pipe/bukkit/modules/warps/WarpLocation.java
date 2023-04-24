package fr.lacaleche.pipe.bukkit.modules.warps;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;
import fr.lacaleche.pipe.Pipe;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

@Serializer(variables = {"world", "x", "y", "z", "yaw", "pitch"})
public class WarpLocation {

    @JsonProperty("world")
    private String world;
    @JsonProperty("x")
    private double x;
    @JsonProperty("y")
    private double y;
    @JsonProperty("z")
    private double z;
    @JsonProperty("yaw")
    private float yaw;
    @JsonProperty("pitch")
    private float pitch;

    public WarpLocation() {
    }

    public WarpLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public Location toLocation() {
        Plugin plugin = Pipe.getBukkit().getPlugin();
        return new Location(plugin.getServer().getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
