package fr.lacaleche.pipe.common.commands.argument;

import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;

public class ArgumentsImpl implements Arguments {

    private ArgumentManager manager;

    public ArgumentsImpl(ArgumentManager manager) {
        this.manager = manager;
    }

    @Override
    public Argument get(String key) {
        return this.manager.getArguments().stream().filter(argument -> argument.getKey().equals(key)).findFirst().orElse(new StringArgument("null"));
    }

    @Override
    public boolean exist(String key) {
        return this.manager.getArguments().stream().anyMatch(argument -> argument.getKey().equals(key));
    }

    @Override
    public boolean blank(String key) {
        return !this.exist(key) ? true : this.get(key).getValue().isBlank();
    }

    @Override
    public boolean mandatory(String key) {
        return this.exist(key) ? manager.getArgument(key).isMandatory() : false;
    }
}
