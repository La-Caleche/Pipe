package fr.lacaleche.pipe.bukkit.modules.god;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.Logger;
import fr.lacaleche.pipe.bukkit.modules.god.damage.DamageModule;
import fr.lacaleche.pipe.bukkit.modules.god.food.FoodModule;
import fr.lacaleche.pipe.bukkit.modules.god.redstone.RedstoneModule;
import fr.lacaleche.pipe.bukkit.modules.god.spawn.SpawnModule;
import fr.lacaleche.pipe.bukkit.modules.god.worldborder.WorldBorderModule;

import java.util.ArrayList;
import java.util.List;

@AModule(target = ModuleTarget.BUKKIT)
public class GodModule extends Module {

    private List<IModule> enabledModules;

    public GodModule(IModuleHandler handler) {
        super(handler);

        enabledModules = new ArrayList<>();
    }

    public void enableGodModule(IModule module) {
        if (enabledModules.contains(module)) {
            String name = module.getClass().getSimpleName().replace("Module", "");
            Logger.warn("God Module %s is already enabled.".formatted(name));
            return;
        }
        enabledModules.add(module);
        this.getHandler().enableModule(module);
    }

    public void disableGodModule(IModule module) {
        if (!enabledModules.contains(module)) {
            String name = module.getClass().getSimpleName().replace("Module", "");
            Logger.warn("God Module %s is not enabled.".formatted(name));
            return;
        }
        enabledModules.remove(module);
        this.getHandler().disableModule(module);
    }

    public void disableGodModule(Class<? extends IModule> moduleClazz) {
        this.disableGodModule(this.getHandler().getModule(moduleClazz));
    }

    public void reloadGodModule(IModule module) {
        if (!enabledModules.contains(module)) {
            String name = module.getClass().getSimpleName().replace("Module", "");
            Logger.warn("God Module %s is not enabled.".formatted(name));
            return;
        }
        enabledModules.remove(module);
        this.getHandler().reloadModule(module);
    }

    public void reloadGodModule(Class<? extends IModule> moduleClazz) {
        this.reloadGodModule(this.getHandler().getModule(moduleClazz));
    }

    public void enableAll() {
        this.enableGodModule(new DamageModule(this.getHandler()));
        this.enableGodModule(new FoodModule(this.getHandler()));
        this.enableGodModule(new RedstoneModule(this.getHandler()));
        this.enableGodModule(new SpawnModule(this.getHandler()));
        this.enableGodModule(new WorldBorderModule(this.getHandler()));
    }

    public void disableAll() {
        this.enabledModules.forEach(this::disableGodModule);
    }

    public void reloadAll() {
        this.enabledModules.forEach(this::reloadGodModule);
    }

}