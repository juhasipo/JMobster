package fi.vincit.jmobster.util;

import java.lang.annotation.Annotation;

/**
 * Test util class for creating annotations
 */
public class AbstractAnnotation implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() { return this.getClass(); }
}
