package fr.lacaleche.pipe.common.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annontation used to define a child
 * for a CoreCommand.
 *
 * @author Malo ALLAIN
 * @since 1.0.0
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandChild {

    /**
     * Define the state of the child.
     *
     * If enabled the child is executable by players or console.
     * If not, the child is hidden for everyone.
     *
     * By default the child is enabled.
     *
     * @since 1.0.0
     *
     * @return true or false depend if command enabled
     * */
    boolean enabled() default true;

    /**
     * Define the child label use to execute the command.
     *
     * Example for command "playerlist" :
     * label = "add"
     * In game look like : /playerlist add [player]
     *
     * @since 1.0.0
     *
     * @return command label
     * */
    String label();

    /**
     * Define the child description used in help page.
     *
     * By default description is defined by "No description"
     *
     * @since 1.0.0
     *
     * @return command description
     * */
    String description() default "No description.";

    /**
     * Define arguments used by the child.
     *
     * Example for command "playerlist" :
     * label = "add",
     * arguments = {"player"}
     * Help look like : /playerlist add [player] : @description
     *
     * @since 1.0.0
     *
     * @return command arguments
     * */
    String[] arguments() default {};

}
