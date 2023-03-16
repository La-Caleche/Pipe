package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.text.DecimalFormat;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class DoubleArgument extends DefaultArgument {

    public DoubleArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        completer.addAll(DoubleStream.iterate(0, n -> n + .1).limit(11).boxed().map(d -> new DecimalFormat("#.#").format(d)).collect(Collectors.toList()));
    }
}
