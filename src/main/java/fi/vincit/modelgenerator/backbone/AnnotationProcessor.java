package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.util.ModelWriter;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationProcessor {
    void writeValidation( List<Annotation> validationAnnotations, ModelWriter writer );
}
