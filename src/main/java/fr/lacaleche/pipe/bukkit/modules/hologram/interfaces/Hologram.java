package fr.lacaleche.pipe.bukkit.modules.hologram.interfaces;

import fr.lacaleche.core.utils.maths.Vector3;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

public interface Hologram {

    HologramController getController();

    Component title();

    void title(Component title);

    void scale(Vector3f scale);

    void setBillboard(CalecheDisplay.BillboardConstraints billboard);

    void remove();

    void move(Location location);

    void showTo(Player player);

    void hideTo(Player player);

    void create();

}
