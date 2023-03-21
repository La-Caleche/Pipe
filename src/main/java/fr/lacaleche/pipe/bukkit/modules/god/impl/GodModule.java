package fr.lacaleche.pipe.bukkit.modules.god.impl;

import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;

@AModule(target = ModuleTarget.BUKKIT)
public class GodModule extends BukkitModule {

    private final fr.lacaleche.pipe.bukkit.modules.god.GodModule godModule;

    public GodModule(fr.lacaleche.pipe.bukkit.modules.god.GodModule parent) {
        super(parent.getHandler());
        this.godModule = parent;
    }

}
