package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

public class RegisteredCommandArgument extends DefaultArgument {


    boolean withAliases = false;

    public RegisteredCommandArgument(String key) {
        super(key);
    }

    public RegisteredCommandArgument withAliases(boolean withAliases) {
        this.withAliases = withAliases;
        return this;
    }

    @Override
    public void completer(Completer completer) {
        Pipe.get().getCommandManager().getNetworkCommands().forEach((s, strings) -> strings.forEach((cmd) -> {
            if (cmd.startsWith("∅") && !withAliases) return;
            completer.add(cmd);
        }));
    }
}
