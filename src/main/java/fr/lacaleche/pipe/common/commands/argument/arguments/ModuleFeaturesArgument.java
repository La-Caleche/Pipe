package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.stream.Collectors;

public class ModuleFeaturesArgument extends DefaultArgument {

    public ModuleFeaturesArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        CalecheCore core = CalecheCore.get();
        IModule module = core.getCentralModuleManager().getAnyModule(completer.getArgumentManager().getArgument("module").getValue());

        if (module != null) {
            completer.addAll(module.getFeatures().keySet().stream().map(Object::toString).collect(Collectors.toSet()));
        }

    }

}
