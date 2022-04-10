package fr.lacaleche.pipe.common.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define a custom command
 *
 * @since 1.0.0
 *
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MinecraftCommand {

    /**
     * Define the state of the command.
     *
     * If enabled the command is executable by players or console.
     * If not, the command is hidden for everyone.
     *
     * By default the command is enabled.
     *
     * @since 1.0.0
     *
     * @return true or false depend if enabled
     * */
    boolean enabled() default true;

    /**
     * Define the command label use to execute the command.
     *
     * Example :
     * label = "invsee"
     * In game look like : /invsee [player]
     *
     * @since 1.0.0
     *
     * @return the command label
     * */
    String label();

    /**
     * Define the command description used in help page.
     *
     * By default description is defined by "No description"
     *
     * @since 1.0.0
     *
     * @return the command description
     * */
    String description() default "No description.";

    /**
     * Define the aliases for this command.
     *
     * Example :
     * label = "invsee",
     * aliases = {"isee", "playerinv"}
     * In game look like : /isee [player] or /playerinv [player]
     * instead of /invsee [player]
     *
     * By default no aliases is defined.
     *
     * @since 1.0.0
     *
     * @return command aliases
     * */
    String[] aliases() default {};

    /**
     * Define arguments used by the command.
     *
     * Example :
     * label = "invsee",
     * arguments = {"player"}
     * Help look like : /invsee [player] : @description
     *
     * @since 1.0.0
     *
     * @return command arguments
     * */
    String[] arguments() default {};

}
