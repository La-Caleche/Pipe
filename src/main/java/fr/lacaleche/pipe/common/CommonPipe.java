package fr.lacaleche.pipe.common;

import fr.lacaleche.pipe.AbstractPipe;
import fr.lacaleche.pipe.Pipe;

public class CommonPipe extends AbstractPipe {

    public static Pipe instance;

    public static Pipe get() {
        if (instance == null) {
            instance = new CommonPipe();
        }
        return instance;
    }

}
