package fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.HephaestusModule;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import team.unnamed.hephaestus.bukkit.ModelView;

public class AnimationsArgument extends DefaultArgument {

    public AnimationsArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        HephaestusModule module = Core.getModule(HephaestusModule.class);
        ModelView entity = module.getManager().getModelEntityById(completer.getArgumentManager().getArgument("view").getValue());
        if (entity != null) {
            completer.addAll(entity.model().animations().keySet());
        }
    }

}
