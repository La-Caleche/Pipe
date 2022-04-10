package fr.lacaleche.pipe.common.commands.argument;

import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;

import java.util.ArrayList;
import java.util.List;

public class ArgumentManagerImpl implements ArgumentManager {

    private final Class<?> command;
    private final List<Argument> arguments;

    public ArgumentManagerImpl(Class<?> command) {
        this.command = command;
        this.arguments = new ArrayList<>();
    }

    @Override
    public Class<?> getCommand() {
        return this.command;
    }

    @Override
    public List<Argument> getArguments() {
        return this.arguments;
    }

    @Override
    public Argument getArgument(int index) {
        return getArguments().get(index);
    }

    @Override
    public Argument getArgument(String key) {
        return arguments.stream().filter(argument -> argument.getKey().equals(key)).findFirst().orElse(null);
    }

    @Override
    public Argument addArgument(Argument argument) {
        this.arguments.add(argument);
        return argument;
    }

    @Override
    public void removeArgument(Argument argument) {
        this.arguments.remove(argument);
    }

}
