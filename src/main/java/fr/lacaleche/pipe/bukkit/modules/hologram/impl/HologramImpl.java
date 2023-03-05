package fr.lacaleche.pipe.bukkit.modules.hologram.impl;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.hologram.HologramManager;
import fr.lacaleche.pipe.bukkit.modules.hologram.HologramModule;
import fr.lacaleche.pipe.bukkit.modules.hologram.interfaces.Hologram;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSManager;
import fr.lacaleche.pipe.bukkit.modules.nms.NMSModule;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HologramImpl implements Hologram {

    private HologramController controller;
    private Component title;
    private Collection<Player> viewers;

    private HologramManager hologramManager;

    public HologramImpl(Location location, Component title) {
        if (!CalecheCore.get().getCentralModuleManager().moduleEnabled(HologramModule.class)) throw new IllegalStateException("Hologram module is not enabled !");

        this.hologramManager = CalecheCore.get().getCentralModuleManager().getModule(HologramModule.class).getHologramManager();

        this.title = title;
        this.viewers = new ArrayList<>();

        this.controller = CalecheCore.get().getCentralModuleManager().getModule(NMSModule.class).getNmsManager().createEntity(HologramController.class, location);

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
        this.controller.setTitle(title);
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
    public void create() {
        this.controller.spawn();
        this.controller.setTitle(title);
    }

}
