package fi.vincit.jmobster.backbone.annotation;

import fi.vincit.jmobster.BaseValidationAnnotationProcessor;
import fi.vincit.jmobster.util.JavaToJSPatternConverter;
import fi.vincit.jmobster.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;
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
