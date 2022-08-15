package fr.lacaleche.pipe.bukkit.modules.prmodule;

import fr.lacaleche.core.modules.Module;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.prmodule.commands.ModuleCommand;

@AModule(target = ModuleTarget.BUKKIT)
public class ModsManagerModule extends Module {

    public ModsManagerModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerCommands() {
        Pipe.get().getCommandManager().registerNewCommand(this, ModuleCommand.class);
    }
}