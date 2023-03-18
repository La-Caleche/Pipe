package fr.lacaleche.pipe.bukkit.modules.god;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.impl.GodModuleManager;
import fr.lacaleche.pipe.bukkit.modules.god.interfaces.IGodModuleManager;

import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class ParentGodModule extends BukkitModule {

    private final IGodModuleManager godModuleManager;

    public ParentGodModule(IModuleHandler handler) {
        super(handler);

        this.godModuleManager = new GodModuleManager(handler);
    }

    public IGodModuleManager getGodModuleManager() {
        return godModuleManager;
    }

    @Override
    public void onEnable() {
        Pipe.get().getCachedGodModules().forEach(clazz -> {
            this.getGodModuleManager().enableModule(clazz);
        });

        Pipe.get().getCachedGodModules().clear();
    }

    @Override
    public void onDisable() {
        Pipe.get().getCachedGodModules().addAll(this.getGodModuleManager().getGodModules().stream().map(IModule::getClass).toList());
        List<IModule> cached = this.getGodModuleManager().getGodModules();

        cached.forEach(module -> Core.get().getCentralModuleManager().disableModule(module));
        cached.clear();
    }

}