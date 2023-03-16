package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntegerArgument extends DefaultArgument {

    public IntegerArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(IntStream.rangeClosed(0, 10).boxed().map(integer -> integer.toString()).collect(Collectors.toList()));
    }
}
