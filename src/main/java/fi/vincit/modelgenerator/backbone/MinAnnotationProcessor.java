package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;

import javax.validation.constraints.Min;
import java.io.IOException;
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
