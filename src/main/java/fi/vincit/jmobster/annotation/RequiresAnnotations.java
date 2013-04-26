package fi.vincit.jmobster.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for marking JMobster Validators classes that
 * don't take any initialization parameters what are the
 * required annotations.
 *
 * Given annotation classes are the actual annotation classes.
 * E.g. with JSR-303 the real JSR-303 validation annotation.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface RequiresAnnotations {
    public Class[] value();
}
