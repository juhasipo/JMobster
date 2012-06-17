package fi.vincit.jmobster.backbone;

import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationProcessor {
    void writeValidation( List<Annotation> validationAnnotations, ModelWriter writer );
}
