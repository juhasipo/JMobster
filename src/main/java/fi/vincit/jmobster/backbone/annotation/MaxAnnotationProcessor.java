package fi.vincit.jmobster.backbone.annotation;

import fi.vincit.jmobster.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import javax.validation.constraints.Max;
import java.lang.annotation.Annotation;

public class MaxAnnotationProcessor extends BaseValidationAnnotationProcessor {

    public MaxAnnotationProcessor() {
        super("number");
    }

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        writer.write( "max: " ).write( "" + ((Max)annotation).value() );
    }

}
