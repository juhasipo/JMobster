package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class NotNullAnnotationProcessor extends BaseValidationAnnotationProcessor {

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) throws IOException {
        writer.write( "required: true" );
    }
}
