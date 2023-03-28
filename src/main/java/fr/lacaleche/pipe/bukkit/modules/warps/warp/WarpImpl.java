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
import fr.lacaleche.pipe.bukkit.mysql.annotations.BukkitBlob;
import fr.lacaleche.pipe.bukkit.mysql.serializers.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;

public class WarpImpl extends SqlModel implements IWarp {

    @Property
    private String name;
    @Property
    private String host;
    @BukkitBlob(serializer = LocationSerializer.class)
    private Location location;

    public WarpImpl(String name, Location location) {
        super();

        this.name = name;
        this.host = Core.get().getHost();
        this.location = location;
        this.insertOrSave();
        this.cache();
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
        this.save();
    }

    public void teleport(Player player) {
        player.teleport(this.location);
    }
}
