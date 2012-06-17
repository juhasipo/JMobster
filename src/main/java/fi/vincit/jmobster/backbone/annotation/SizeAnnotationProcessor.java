package fi.vincit.jmobster.backbone.annotation;

import fi.vincit.jmobster.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;

public class SizeAnnotationProcessor extends BaseValidationAnnotationProcessor {

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        Size size = (Size)annotation;
        writer.write( "minlength: " ).writeLine( "" + size.min() + "," );
        writer.write( "maxlength: " ).write( "" + size.max() );
    }

}
