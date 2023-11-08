package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntegerArgument extends AbstractNumberArgument<Integer> {

    public IntegerArgument(String key) {
        super(key);

        this.min(0);
        this.max(9);
        this.step(1);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(IntStream.rangeClosed(this.min(), this.max() / this.step())
                .boxed()
                .map(i -> i * this.step())
                .map(Object::toString)
                .collect(Collectors.toList()));
    }
}
