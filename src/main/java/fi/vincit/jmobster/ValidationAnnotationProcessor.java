package fi.vincit.jmobster;

import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;

public interface ValidationAnnotationProcessor {
    void writeValidatorsToStream( Annotation annotation, ModelWriter writer );
    void validateType(String type);
    String requiredType();
    boolean requiresType();
}
