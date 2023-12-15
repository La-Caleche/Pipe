package fr.lacaleche.pipe.bukkit.modules.god.interfaces;

import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.IModuleManager;

import java.util.List;

public interface IGodModuleManager extends IModuleManager {

    boolean enableModule(Class<? extends IModule> moduleClazz);

    void disableModule(IModule module);

    List<IModule> getGodModules();

}
