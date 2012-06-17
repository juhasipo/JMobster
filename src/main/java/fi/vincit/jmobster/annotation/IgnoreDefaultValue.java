package fi.vincit.jmobster.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Force processor to ignore the default value for the annotated
 * property. This is usually needed when the property is an
 * ID and it shouldn't have a default value on client side.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD})
public @interface IgnoreDefaultValue {
}
