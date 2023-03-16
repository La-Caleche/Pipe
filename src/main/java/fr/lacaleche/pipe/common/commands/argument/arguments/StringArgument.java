package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class StringArgument extends DefaultArgument {

    public StringArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {}

}
