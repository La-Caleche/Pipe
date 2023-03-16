package fr.lacaleche.pipe.common.commands.argument;

import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.argument.interfaces.Completer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CompleterImpl implements Completer {

    private Object sender;
    private CoreCommandImpl coreCommand;
    private ArgumentManager argumentManager;
    private boolean validation;
    private List<String> completer;
    private int index;
    private boolean next;

    public CompleterImpl(CoreCommandImpl coreCommand, Object sender) {
        this.coreCommand = coreCommand;
        this.sender = sender;
        this.validation = true;
        this.argumentManager = coreCommand.getManager();
        this.completer = new ArrayList<>();
    }

    @Override
    public Object sender() {
        return this.sender;
    }

    @Override
    public CoreCommandImpl getCoreCommand() {
        return coreCommand;
    }

    @Override
    public ArgumentManager getArgumentManager() {
        return this.argumentManager;
    }

    @Override
    public List<String> getCompleter() {
        return this.completer;
    }

    @Override
    public void add(String value) {
        this.completer.add(value);
    }

    @Override
    public void add(int value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(long value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(float value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(double value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(boolean value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(short value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(byte value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(char value) {
        this.completer.add(String.valueOf(value));
    }

    @Override
    public void add(Object value) {
        this.completer.add(value.toString());
    }

    @Override
    public void addAll(Collection<? extends String> values) {
        this.completer.addAll(values);
    }

    @Override
    public void addAll(String[] values) {
        this.completer.addAll(Arrays.stream(values).toList());
    }

    @Override
    public void setCompleter(List<String> completer) {
        this.completer = completer;
    }

    @Override
    public boolean doValidation() {
        return this.validation;
    }

    @Override
    public void cancelValidation() {
        this.validation = false;
    }

    @Override
    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    @Override
    public int index() {
        return this.index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void incrementIndex() {
        this.index ++;
    }

    @Override
    public boolean next() {
        return next;
    }

    @Override
    public void setNext(boolean next) {
        this.next = next;
    }
}
