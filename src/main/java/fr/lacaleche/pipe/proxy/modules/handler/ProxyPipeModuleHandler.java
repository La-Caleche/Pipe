package fr.lacaleche.pipe.proxy.modules.handler;

import fr.lacaleche.pipe.common.modules.handler.MinecraftModuleHandler;
import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.proxy.events.ProxyPipeListenerManager;

public class ProxyPipeModuleHandler extends MinecraftModuleHandler {

    @Override
    public void disableModule(IModule module) {
        super.disableModule(module);

        if (CalecheCore.get().getListenerManager() instanceof ProxyPipeListenerManager manager) {
            manager.unregisterProxyListeners(module);
        }
    }
    
}
