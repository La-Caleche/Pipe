package fr.lacaleche.pipe.common.commands.argument.interfaces;

import java.util.List;

public interface ArgumentManager {

    public Class<?> getCommand();

    public List<Argument> getArguments();

    public Argument getArgument(int index);

    public Argument getArgument(String key);

    public void removeArgument(Argument argument);

    public Argument addArgument(Argument argument);

}
