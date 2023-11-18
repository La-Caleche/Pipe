package fr.lacaleche.pipe.bukkit.modules;

import fr.lacaleche.core.modules.FeatureManagerImpl;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.common.commands.utils.PipeDebug;

public class BukkitFeatureManager extends FeatureManagerImpl {

    @Override
    public <T> void cancelEvent(Object event, IFeature<T> feature, String message) {
        PipeDebug.setCancelled(event, true, () -> Logger.customDebugWCheck(this.parseMessage(event, feature, message)));
    }

}
