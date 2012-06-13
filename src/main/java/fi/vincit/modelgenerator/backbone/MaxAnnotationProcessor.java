package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;

import javax.validation.constraints.Max;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class MaxAnnotationProcessor extends BaseValidationAnnotationProcessor {

    public MaxAnnotationProcessor() {
        super("number");
    }

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) throws IOException {
        writer.write( "max: " ).write( "" + ((Max)annotation).value() );
    }

}
