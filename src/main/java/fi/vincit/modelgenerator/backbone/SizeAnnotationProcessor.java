package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;

import javax.validation.constraints.Size;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class SizeAnnotationProcessor extends BaseValidationAnnotationProcessor {

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) throws IOException {
        Size size = (Size)annotation;
        writer.write( "minlength: " ).writeLine( "" + size.min() + "," );
        writer.write( "maxlength: " ).write( "" + size.max() );
    }

}
