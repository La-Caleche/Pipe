package fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments;

import fr.lacaleche.core.Core;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.HephaestusModule;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;
import team.unnamed.hephaestus.Bone;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.BoneView;
import team.unnamed.hephaestus.bukkit.ModelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BonesArgument extends DefaultArgument {

    public BonesArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        HephaestusModule module = Core.getModule(HephaestusModule.class);
        ModelView view = module.getManager().getModelEntityById(completer.getArgumentManager().getArgument("view").getValue());
        if (view != null) {
            completer.addAll(module.getManager().getBones(view).stream().map(Bone::name).collect(Collectors.toList()));
        }
    }

}
