package fi.vincit.jmobster.backbone.annotation;

import fi.vincit.jmobster.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import javax.validation.constraints.Min;
import java.lang.annotation.Annotation;

public class MinAnnotationProcessor extends BaseValidationAnnotationProcessor {

    public MinAnnotationProcessor() {
        super("number");
    }

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        writer.write( "min: " ).write( "" + (( Min )annotation).value() );
    }

}
