package fi.vincit.jmobster.backbone;

import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;
import java.util.List;

public class TestAnnotationProcessor implements AnnotationProcessor {
    @Override
    public void writeValidation( List<Annotation> validationAnnotations, ModelWriter writer ) {
        if( validationAnnotations.size() > 0 ) {
            writer.write("TEST");
        }
    }
}
