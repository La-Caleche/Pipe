package fr.lacaleche.pipe.bukkit.modules.warps.arguments;

import fr.lacaleche.core.databases.generic.ModelFilter;
import fr.lacaleche.pipe.bukkit.modules.warps.warp.WarpImpl;
import fr.lacaleche.pipe.common.commands.argument.arguments.DefaultArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class WarpArgument extends DefaultArgument {

    public WarpArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(new ModelFilter<WarpImpl>().list(WarpImpl.class, true).map(WarpImpl::getName).toList());
    }
}
