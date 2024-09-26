package fr.lacaleche.pipe.common.adventure.placeholder;

import java.util.List;
import java.util.Set;

public class PlaceHolderArgumentsImpl implements PlaceHolderArguments {

    private final Set<?> arguments;

    public PlaceHolderArgumentsImpl(List<?> arguments) {
        this.arguments = Set.copyOf(arguments);
    }

    @Override
    public boolean hasNext() {
        return this.arguments.iterator().hasNext();
    }

    @Override
    public <T> T next() {
        return (T) this.arguments.iterator().next();
    }

    @Override
    public int size() {
        return this.arguments.size();
    }

}
