package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.util.ModelWriter;

import java.io.IOException;
import java.lang.annotation.Annotation;

public interface ValidationAnnotationProcessor {
    void writeValidatorsToStream( Annotation annotation, ModelWriter writer );
    void validateType(String type);
    String requiredType();
    boolean requiresType();
}
