package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.ValidationAnnotationProcessor;

import java.lang.annotation.Annotation;

public interface AnnotationProcessorProvider {
    ValidationAnnotationProcessor getValidator( Annotation annotation );
    boolean isAnnotationForValidation(Annotation annotation);
}
