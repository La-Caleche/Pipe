package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.stream.Collectors;

public class ModuleFeaturesArgument extends DefaultArgument {

    public ModuleFeaturesArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        Core core = Core.get();
        IModule module = core.getCentralModuleManager().getAnyModule(completer.getArgumentManager().getArgument("module").getValue());

        if (module != null && module.getFeatureManager() != null) {
            completer.addAll(module.getFeatureManager().getFeatures().stream().map(IFeature::name).collect(Collectors.toSet()));
        }

    }

}
