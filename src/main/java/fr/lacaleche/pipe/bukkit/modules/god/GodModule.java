package fr.lacaleche.pipe.bukkit.modules.god;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.interfaces.IGodModuleManager;

import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class GodModule extends BukkitModule {

    private final IGodModuleManager godModuleManager;

    public GodModule(IModuleHandler handler) {
        super(handler);

        this.godModuleManager = new GodModuleManager(handler);
    }

    public IGodModuleManager getGodModuleManager() {
        return godModuleManager;
    }

    @Override
    public void onEnable() {
        Pipe.getBukkit().getCachedGodModules().forEach(clazz -> {
            this.getGodModuleManager().enableModule(clazz);
        });

        Pipe.getBukkit().getCachedGodModules().clear();
    }

    @Override
    public void onDisable() {
        Pipe.getBukkit().getCachedGodModules().addAll(this.getGodModuleManager().getGodModules().stream().map(IModule::getClass).toList());
        List<IModule> cached = this.getGodModuleManager().getGodModules();

        cached.forEach(module -> Core.get().getCentralModuleManager().disableModule(module));
        cached.clear();
    }

}