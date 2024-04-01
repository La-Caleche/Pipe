package fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.HephaestusModule;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class ModelsArgument extends DefaultArgument {

    public ModelsArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        HephaestusModule module = Core.getModule(HephaestusModule.class);
        completer.addAll(module.getManager().getModelRegistry().modelNames());
    }

}
