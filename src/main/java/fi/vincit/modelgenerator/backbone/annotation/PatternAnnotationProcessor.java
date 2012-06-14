package fi.vincit.modelgenerator.backbone.annotation;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class PatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( PatternAnnotationProcessor.class );

    private static final String JAVASCRIPT_REGEXP_START = "/";
    private static final String JAVASCRIPT_REGEXP_END = "/";
    // JavaScrip regular expression modifiers
    private static final String JAVASCRIPT_CASE_INSENSITIVE_MOD = "i";
    private static final String JAVASCRIPT_MULTILINE_MOD = "m";

    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) {
        Pattern pattern = (Pattern)annotation;
        String javaScriptRegExp = convertToJavaScriptRegExp( pattern.regexp(), pattern.flags() );
        writer.write( "pattern: " ).write( javaScriptRegExp );
    }

    private String convertToJavaScriptRegExp( String javaRegExp, Pattern.Flag... flags ) {
        return JAVASCRIPT_REGEXP_START + javaRegExp + JAVASCRIPT_REGEXP_END + getModifiersFromFlags(flags);
    }

    private String getModifiersFromFlags( Pattern.Flag... flags ) {
        String modifiers = "";
        for( Pattern.Flag flag : flags ) {
            if( Pattern.Flag.CASE_INSENSITIVE.equals(flag) ) {
                modifiers += JAVASCRIPT_CASE_INSENSITIVE_MOD;
            } else if( Pattern.Flag.MULTILINE.equals(flag) ) {
                modifiers += JAVASCRIPT_MULTILINE_MOD;
            } else {
                LOG.warn("Regular Expression flag " + flag.name() + " is not supported");
            }
        }
        return modifiers;
    }
}
