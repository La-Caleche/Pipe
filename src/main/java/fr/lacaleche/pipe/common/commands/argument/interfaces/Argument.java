package fr.lacaleche.pipe.common.commands.argument.interfaces;

public interface Argument {

    public String getKey();

    public Argument setValue(String value);

    public String getValue();

    public boolean isMandatory();

    public Argument setMandatory(boolean mandatory);

    public Argument setMultiple(boolean multiple);

    public boolean isMultiple();

    public void completer(Completer completer);

}
