package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.utils.CalecheDebug;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class DisabledModuleArgument extends DefaultArgument {

    public DisabledModuleArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(CalecheCore.get().getCentralModuleManager().getDisabledModules().stream().map((clazz) -> CalecheDebug.shortPackage(clazz.getName())).toList());
    }

}
