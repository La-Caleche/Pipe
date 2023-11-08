package fr.lacaleche.pipe.common.commands.argument.arguments;

import fr.lacaleche.pipe.common.commands.argument.interfaces.NumberArgument;

public abstract class AbstractNumberArgument<T> extends DefaultArgument implements NumberArgument<T> {

    private T min;
    private T max;
    private T step;

    public AbstractNumberArgument(String key) {
        super(key);
    }

    @Override
    public NumberArgument<T> min(T t) {
        this.min = t;
        return this;
    }

    @Override
    public NumberArgument<T> max(T t) {
        this.max = t;
        return this;
    }

    @Override
    public NumberArgument<T> step(T t) {
        this.step = t;
        return this;
    }

    @Override
    public T min() {
        return this.min;
    }

    @Override
    public T max() {
        return this.max;
    }

    @Override
    public T step() {
        return this.step;
    }
}
