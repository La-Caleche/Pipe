package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class RegisteredCommandArgument extends DefaultArgument {

    public RegisteredCommandArgument(String key) {
        super(key);
    }

    @Override
    public void completer(Completer completer) {
        Pipe.get().getCommandManager().getCommands().forEach((label, commandClass) -> completer.add(label));
    }
}
