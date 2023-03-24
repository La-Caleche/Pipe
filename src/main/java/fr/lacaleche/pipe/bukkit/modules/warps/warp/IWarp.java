package fr.lacaleche.pipe.bukkit.modules.warps.warp;

import fr.lacaleche.core.databases.mysql.models.annotations.Entity;
import org.bukkit.Location;

@Entity("warps")
public interface IWarp {
    String getName();

    void setName(String name);

    String getHost();

    void setHost(String host);

    Location getLocation();

    void setLocation(Location location);
}
