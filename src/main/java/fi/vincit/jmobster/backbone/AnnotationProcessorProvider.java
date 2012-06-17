package fi.vincit.jmobster.backbone;

import fi.vincit.jmobster.ValidationAnnotationProcessor;

import java.lang.annotation.Annotation;

public interface AnnotationProcessorProvider {
    ValidationAnnotationProcessor getValidator( Annotation annotation );
    boolean isAnnotationForValidation(Annotation annotation);
}
