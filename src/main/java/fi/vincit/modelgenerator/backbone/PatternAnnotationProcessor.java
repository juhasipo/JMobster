package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.BaseValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class PatternAnnotationProcessor extends BaseValidationAnnotationProcessor {
    @Override
    public void writeValidatorsToStream( Annotation annotation, ModelWriter writer ) throws IOException {
        Pattern pattern = (Pattern)annotation;
        String javaScriptRegExp = convertToJavaScriptRegExp( pattern.regexp(), pattern.flags() );
        writer.write( "pattern: " ).write( javaScriptRegExp );
    }

    private String convertToJavaScriptRegExp( String javaRegExp, Pattern.Flag... flags ) {
        return "/" + javaRegExp + "/";
    }
}
