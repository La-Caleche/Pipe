package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.Arrays;

public class BooleanArgument extends DefaultArgument {

    public BooleanArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(Arrays.asList("true", "false"));
    }
}
