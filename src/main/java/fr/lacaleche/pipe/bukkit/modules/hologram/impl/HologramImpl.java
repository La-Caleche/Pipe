package fr.lacaleche.pipe.bukkit.modules.hologram.impl;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.bukkit.modules.hologram.HologramManager;
import fr.lacaleche.pipe.bukkit.modules.hologram.HologramModule;
import fr.lacaleche.pipe.bukkit.modules.hologram.interfaces.Hologram;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.CalecheDisplay;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

public class HologramImpl implements Hologram {

    private HologramController controller;
    private Component title;
    private Vector3f scale;
    private CalecheDisplay.BillboardConstraints billboard;
    private Collection<Player> viewers;

    private HologramManager hologramManager;

    public HologramImpl(Location location, Component title) {
        this(location, title, new Vector3f(1f, 1f, 1f), CalecheDisplay.BillboardConstraints.CENTER);
    }

    public HologramImpl(Location location, Component title, Vector3f scale) {
        this(location, title, scale, CalecheDisplay.BillboardConstraints.CENTER);
    }

    public HologramImpl(Location location, Component title, CalecheDisplay.BillboardConstraints constraints) {
        this(location, title, new Vector3f(1f, 1f, 1f), constraints);
    }

    public HologramImpl(Location location, Component title, Vector3f scale, CalecheDisplay.BillboardConstraints constraints) {
        if (!Core.get().getCentralModuleManager().moduleEnabled(HologramModule.class)) throw new IllegalStateException("Hologram module is not enabled !");

        this.hologramManager = Core.getModule(HologramModule.class).getHologramManager();

        this.title = title;
        this.scale = scale;
        this.billboard = constraints;
        this.viewers = new ArrayList<>();

        this.controller = Core.getModule(NMSModule.class).getNmsManager().createEntity(HologramController.class, location);

        this.hologramManager.registerHologram(this);
    }

    @Override
    public HologramController getController() {
        return this.controller;
    }

    @Override
    public Component title() {
        return this.title;
    }

    @Override
    public void title(Component title) {
        this.title = title;
        this.controller.text(title);
    }

    @Override
    public void scale(Vector3f scale) {
        this.scale = scale;
        this.controller.scale(scale);
    }

    @Override
    public void setBillboard(CalecheDisplay.BillboardConstraints billboard) {
        this.billboard = billboard;
        this.controller.setBillboard(billboard);
    }

    @Override
    public void remove() {
        this.viewers.forEach(this.controller::hide);
        this.controller.remove();
        this.hologramManager.unregisterHologram(this);
    }

    @Override
    public void move(Location location) {
        this.controller.setLocation(location);
    }

    @Override
    public void showTo(Player player) {
        this.viewers.add(player);
        this.controller.show(player);
    }

    @Override
    public void hideTo(Player player) {
        this.viewers.remove(player);
        this.controller.hide(player);
    }

    @Override
    public void create() {
        this.controller.spawn();

        this.title(this.title);
        this.scale(this.scale);
        this.setBillboard(this.billboard);
    }

}
