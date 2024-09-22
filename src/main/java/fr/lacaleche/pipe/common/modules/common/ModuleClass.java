package fr.lacaleche.pipe.common.modules.common;

import fr.lacaleche.core.modules.interfaces.IModule;

public interface ModuleClass {

    Class<? extends IModule> clazz();

    static ModuleClass of(final Class<? extends IModule> clazz) {
        return () -> clazz;
    }

}
