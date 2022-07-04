package fr.lacaleche.pipe.common.commands.argument.interfaces;

import fr.lacaleche.pipe.common.commands.CoreCommandImpl;

import java.util.Collection;
import java.util.List;

public interface Completer {

    Object sender();

    CoreCommandImpl getCoreCommand();

    ArgumentManager getArgumentManager();

    List<String> getCompleter();

    int index();

    void incrementIndex();

    void setIndex(int index);

    boolean next();

    void setNext(boolean next);

    void add(String value);

    void addAll(Collection<? extends String> values);

    void addAll(String[] values);

    void setCompleter(List<String> completer);

    boolean doValidation();

    void cancelValidation();

    void setValidation(boolean validation);

}
