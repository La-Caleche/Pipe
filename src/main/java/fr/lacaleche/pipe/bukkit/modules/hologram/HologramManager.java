package fr.lacaleche.pipe.bukkit.modules.hologram;

import fr.lacaleche.pipe.bukkit.modules.hologram.interfaces.Hologram;
import fr.lacaleche.pipe.bukkit.modules.nms.entities.controllers.HologramController;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {

    private List<Hologram> holograms;

    public HologramManager() {
        this.holograms = new ArrayList<>();
    }

    public void registerHologram(Hologram hologram) {
        if (this.holograms == null || this.holograms.contains(hologram)) return;
        this.holograms.add(hologram);
    }

    public void unregisterHologram(Hologram hologram) {
        if (this.holograms == null || !this.holograms.contains(hologram)) return;
        this.holograms.remove(hologram);
    }

    public void disable() {
        final List<Hologram> cache = new ArrayList<>(this.getHolograms());
        cache.forEach(Hologram::remove);
        cache.clear();

        this.holograms.clear();
        this.holograms = null;
    }

    public List<Hologram> getHolograms() {
        return holograms;
    }
}
