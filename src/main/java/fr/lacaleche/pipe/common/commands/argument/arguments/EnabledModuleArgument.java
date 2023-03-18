package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class EnabledModuleArgument extends DefaultArgument {

    public EnabledModuleArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Core.get().getCentralModuleManager().getEnabledModules().stream().map((clazz) -> CalecheDebug.shortPackage(clazz.getName())).toList());
    }

}
