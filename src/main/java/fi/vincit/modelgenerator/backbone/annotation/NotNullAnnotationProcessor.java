package fi.vincit.modelgenerator.backbone.annotation;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class NotNullAnnotationProcessor extends BaseValidationAnnotationProcessor {

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        writer.write( "required: true" );
    }
}
