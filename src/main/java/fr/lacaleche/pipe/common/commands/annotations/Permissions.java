package fr.lacaleche.pipe.common.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permissions {

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
