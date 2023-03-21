package fr.lacaleche.pipe.bukkit.modules.warps.warp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lacaleche.core.Core;
import fr.lacaleche.core.databases.mysql.models.SqlModel;
import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import fr.lacaleche.core.databases.mysql.models.annotations.Property;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.core.utils.serializer.interfaces.CoreSerializer;
import fr.lacaleche.pipe.bukkit.modules.warps.WarpLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;

public class WarpImpl extends SqlModel implements IWarp {

    @Property
    private String name;
    @Property
    private String host;
    @Property(column = "location")
    private String serializedLocation;
    private Location location;

    public WarpImpl(String name, Location location) {
        this.name = name;
        this.host = Core.get().getHost();
        this.location = location;
        this.serializedLocation = this.serializeLocation();
        this.save();
        this.insert();
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
        this.serializedLocation = this.serializeLocation();
        this.save();
    }

    @Override
    public void loaded() {
        this.location = this.deserializeLocation();
    }
    private String serializeLocation() {
        return CoreSerializer.get().serialize(new WarpLocation(this.location)).get();
    }

    private Location deserializeLocation() {
//        return CoreSerializer.get().deserialize(WarpLocation.class, this.serializedLocation).get().toLocation();
        try {
            return new ObjectMapper().readValue(this.serializedLocation, WarpLocation.class).toLocation();
        } catch (IOException e) {
            SentryAPIImpl.getInstance().captureException(e);
            return null;
        }
    }

    public void teleport(Player player) {
        player.teleport(this.location);
    }
}
