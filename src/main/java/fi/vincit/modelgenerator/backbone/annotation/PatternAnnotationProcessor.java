package fi.vincit.modelgenerator.backbone.annotation;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.JavaToJSPatternConverter;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class PatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( PatternAnnotationProcessor.class );

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        Pattern pattern = (Pattern)annotation;
        String javaScriptRegExp = JavaToJSPatternConverter.convertFromJava( pattern.regexp(), pattern.flags() );
        writer.write( "pattern: " ).write( javaScriptRegExp );
    }

}
