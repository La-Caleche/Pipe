package fr.lacaleche.pipe.common.commands.argument.interfaces;

public interface NumberArgument<T> extends Argument {

    NumberArgument<T> min(T t);

    NumberArgument<T> max(T t);

    NumberArgument<T> step(T t);

    T min();

    T max();

    T step();

}
