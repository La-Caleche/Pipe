package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.Argument;

public abstract class DefaultArgument implements Argument {

    private final String key;
    private String value;
    private boolean mandatory;
    private boolean multiple;

    public DefaultArgument(String key) {
        this.key = key;
        this.value = "";
        this.mandatory = true;
        this.multiple = false;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean isMandatory() {
        return this.mandatory;
    }

    @Override
    public Argument setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    @Override
    public Argument optional() {
        return this.setMandatory(false);
    }

    @Override
    public Argument setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public Argument setMultiple(boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    @Override
    public boolean isMultiple() {
        return this.multiple;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
