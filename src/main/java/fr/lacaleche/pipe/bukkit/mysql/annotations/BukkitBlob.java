package fr.lacaleche.pipe.bukkit.mysql.annotations;

import fr.lacaleche.core.databases.mysql.morph.serializer.interfaces.Serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface BukkitBlob {

    String column() default ".";

    Class<? extends Serializer> serializer();

}
