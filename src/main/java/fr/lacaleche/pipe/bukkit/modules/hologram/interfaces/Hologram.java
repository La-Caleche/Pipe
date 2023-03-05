package fr.lacaleche.pipe.bukkit.modules.hologram.interfaces;

import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Hologram {

    HologramController getController();

    Component title();

    void title(Component title);

    void remove();

    void move(Location location);

    void showTo(Player player);

    void create();

}
