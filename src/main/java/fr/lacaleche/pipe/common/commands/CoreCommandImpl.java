package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.common.commands.enums.CommandResult;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;

public class CoreCommandImpl {

    private String label;
    private Class<?> command;
    private ArgumentManager manager;
    private Object commandSender;
    private String userInput;
    private String[] userArguments;

    public CoreCommandImpl(String label, Object commandSender, Class<?> command, ArgumentManager manager) {
        this.label = label;
        this.commandSender = commandSender;
        this.command = command;
        this.manager = manager;
    }

    public CommandResult execute() {
        return Pipe.get().getCommandManager().executeCommand(getCommandSender(), getCommand(), getManager());
    }


    public String[] getUserArguments() {
        return userArguments;
    }

    public void setUserArguments(String[] userArguments) {
        this.userArguments = userArguments;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getLabel() {
        return label;
    }

    public Class<?> getCommand() {
        return command;
    }

    public ArgumentManager getManager() {
        return manager;
    }

    public Object getCommandSender() {
        return commandSender;
    }

}
