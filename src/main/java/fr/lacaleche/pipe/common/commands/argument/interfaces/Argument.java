package fr.lacaleche.pipe.common.commands.argument.interfaces;

public interface Argument {

    String getKey();

    Argument setValue(String value);

    String getValue();

    boolean isMandatory();

    Argument setMandatory(boolean mandatory);

    Argument optional();

    Argument setMultiple(boolean multiple);

    boolean isMultiple();

    void completer(Completer completer);

}
