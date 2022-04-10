package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class CustomArgument extends DefaultArgument {

    public CustomArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        Pipe.get().getCommandManager().customCompleter(completer);
    }
}
