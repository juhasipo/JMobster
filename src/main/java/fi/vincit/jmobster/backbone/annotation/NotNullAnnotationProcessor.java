package fi.vincit.jmobster.backbone.annotation;

import fi.vincit.jmobster.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;

public class NotNullAnnotationProcessor extends BaseValidationAnnotationProcessor {

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        writer.write( "required: true" );
    }
}
