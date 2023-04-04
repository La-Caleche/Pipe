package fr.lacaleche.pipe.common.adventure.placeholder;

public interface PlaceHolderArguments {

    boolean hasNext();

    <T> T next();

    int size();

}
