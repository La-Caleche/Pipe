package fr.lacaleche.pipe.common.commands.argument.interfaces;

import java.util.List;

public interface ArgumentManager {

    Class<?> getCommand();

    List<Argument> getArguments();

    Argument getArgument(int index);

    Argument getArgument(String key);

    void removeArgument(Argument argument);

    Argument addArgument(Argument argument);

}
