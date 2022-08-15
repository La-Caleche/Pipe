package fr.lacaleche.pipe.common.commands.argument.interfaces;

import fr.lacaleche.pipe.common.commands.CoreCommandImpl;
import org.bukkit.event.entity.EntityDamageEvent;

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

    void add(int value);

    void add(long value);

    void add(float value);

    void add(double value);

    void add(boolean value);

    void add(short value);

    void add(byte value);

    void add(char value);

    void add(Object value);

    void addAll(Collection<? extends String> values);

    void addAll(String[] values);

    void setCompleter(List<String> completer);

    boolean doValidation();

    void cancelValidation();

    void setValidation(boolean validation);
}
