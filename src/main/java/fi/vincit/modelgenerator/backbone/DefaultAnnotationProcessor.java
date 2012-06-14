package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.ValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ItemProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultAnnotationProcessor implements AnnotationProcessor {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultAnnotationProcessor.class );

    private AnnotationProcessorProvider annotationProcessorProvider;

    public DefaultAnnotationProcessor( AnnotationProcessorProvider annotationProcessorProvider ) {
        this.annotationProcessorProvider = annotationProcessorProvider;
    }

    private static void appendType(boolean hasType, ModelWriter sb, String type) {
        if( !hasType ) {
            sb.write( "type: \"" ).write( type ).writeLine( "\"," );
        }
    }

    @Override
    public void writeValidation( List<Annotation> validationAnnotations, final ModelWriter writer ) {
        ItemProcessor<Annotation> annotationItemProcessor = new ItemProcessor<Annotation>() {
            boolean hasPropertyTypeSet = false;
            @Override
            protected void process( Annotation annotation, boolean isLast ) {
                ValidationAnnotationProcessor annotationProcessor = annotationProcessorProvider.getValidator( annotation );
                if( annotationProcessor != null ) {
                    if( annotationProcessor.requiresType() ) {
                        appendType(hasPropertyTypeSet, writer, annotationProcessor.requiredType() );
                        hasPropertyTypeSet = true;
                    }
                    annotationProcessor.writeValidatorsToStream( annotation, writer );
                } else {
                    LOG.debug("No validator processor found");
                }

                writer.writeLine("", ",", !isLast);
            }
        };
        annotationItemProcessor.process(validationAnnotations);
    }

    public AnnotationProcessorProvider getAnnotationProcessorProvider() {
        return annotationProcessorProvider;
    }
}
