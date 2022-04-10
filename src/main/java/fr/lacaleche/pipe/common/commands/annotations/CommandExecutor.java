package fr.lacaleche.pipe.common.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that define the Executor for
 * the command or the child command.
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandExecutor {

    /**
     * Enum to define the targeted sender.
     *
     * SERVER : Only server side command
     * PLAYER : Only player side command
     * EVERYONE : Server and Player can execute this command
     *
     * @since 1.0.0
     * */
    public enum Executor {
        SERVER, PLAYER, COMMAND_BLOCK, EVERYONE
    }

    /**
     * Define the state of the executor.
     *
     * By default the executor is enabled.
     *
     * @since 1.0.0
     *
     * @return true or false depend if enabled or not
     * */
    boolean enabled() default true;

    /**
     * Define the targeted sender.
     *
     * By default command can be executed by Everyone.
     * SERVER and PLAYER
     *
     * @since 1.0.0
     *
     * @return authorized executor
     * */
    Executor[] executor() default {Executor.EVERYONE};

    /**
     * permlevel inf|= minPermLevel
     * TODO
     * */
    int minPermLevel() default 0;

    /**
     * permLevel == strictPermLevel
     * TODO
     * */
    int strictPermLevel() default 0;

    /**
     * client rank permissions contains 1+ permissionKeys
     * TODO
     * */
    String[] permissions() default {};

}
