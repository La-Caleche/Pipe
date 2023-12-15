package fr.lacaleche.pipe.bukkit.modules.god;

import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.core.utils.sentry.SentryAPIImpl;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.block.BlockModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.entity.EntityModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.explosion.ExplosionModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.health.HealthModule;
import fr.lacaleche.pipe.bukkit.modules.god.interfaces.IGodModuleManager;
import fr.lacaleche.pipe.bukkit.modules.god.modules.portal.PortalModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.spawn.SpawnModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.weather.WeatherModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.time.TimeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GodModuleManager implements IGodModuleManager {

    private final IModuleHandler handler;
    private final List<IModule> godModules;

    public GodModuleManager(IModuleHandler handler) {
        this.handler = handler;
        this.godModules = new ArrayList<>();
    }

    @Override
    public boolean enableModule(IModule module) {
        if (!module.getClass().isAnnotationPresent(AGodModule.class)) {
            Logger.warn("Module " + module.getClass().getSimpleName() + " is not a god module");
            return false;
        }

        if (!this.handler.enableModule(module)) return false;
        this.godModules.add(module);
        return true;
    }

    @Override
    public boolean enableModule(Class<? extends IModule> moduleClazz) {
        return this.enableModule(Objects.requireNonNull(this.instantiateModule(moduleClazz, this.handler)));
    }

    @Override
    public void disableModule(IModule module) {
        if (!module.getClass().isAnnotationPresent(AGodModule.class)) {
            Logger.warn("Module " + module.getClass().getSimpleName() + " is not a god module");
            return;
        }

        this.handler.disableModule(module);
        this.godModules.remove(module);
    }

    @Override
    public void start() {
        this.enableAllModules();
    }

    @Override
    public void enableAllModules() {
        this.enableModule(new BlockModule(this.handler));
        this.enableModule(new EntityModule(this.handler));
        this.enableModule(new ExplosionModule(this.handler));
        this.enableModule(new HealthModule(this.handler));
        this.enableModule(new PortalModule(this.handler));
        this.enableModule(new SpawnModule(this.handler));
        this.enableModule(new WeatherModule(this.handler));
        this.enableModule(new TimeModule(this.handler));
    }

    public IModuleHandler getHandler() {
        return handler;
    }

    @Override
    public List<IModule> getGodModules() {
        return godModules;
    }

    private IModule instantiateModule(Class<? extends IModule> moduleClazz, IModuleHandler handler) {
        try {
            return moduleClazz.getConstructor(IModuleHandler.class).newInstance(handler);
        } catch (Exception e) {
            SentryAPIImpl.getInstance().captureException(e);
            return null;
        }
    }

}
