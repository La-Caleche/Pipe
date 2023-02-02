package fr.lacaleche.pipe.bukkit.modules.god.impl;

import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.ParentGodModule;

@AModule(target = ModuleTarget.BUKKIT)
public class GodModule extends BukkitModule {

    private final ParentGodModule parentGodModule;

    public GodModule(ParentGodModule parent) {
        super(parent.getHandler());
        this.parentGodModule = parent;
    }

}
