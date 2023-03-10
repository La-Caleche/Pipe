package fr.lacaleche.pipe.common.modules.client;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.databases.mysql.models.packets.ModelSavedPacket;
import fr.lacaleche.core.events.GlobalListenerManager;
import fr.lacaleche.core.events.interfaces.IListenerManager;
import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.packets.HelpPacket;

@AModule(target = ModuleTarget.ALL)
public class CommonClientModule extends Module {

    public CommonClientModule(IModuleHandler handler) {
        super(handler);
    }

}
