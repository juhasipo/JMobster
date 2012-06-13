package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.ValidationAnnotationProcessor;
import fi.vincit.modelgenerator.util.ItemProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationProcessor {

    private static final Logger LOG = LoggerFactory
            .getLogger( AnnotationProcessor.class );

    private static Map<Class, ValidationAnnotationProcessor> annotationProcessors;
    static {
        annotationProcessors = new HashMap<Class, ValidationAnnotationProcessor>();
        addValidator(NotNull.class, new NotNullAnnotationProcessor());
        addValidator(Size.class, new SizeAnnotationProcessor());
        addValidator(Min.class, new MinAnnotationProcessor());
        addValidator(Max.class, new MaxAnnotationProcessor());
        addValidator(Pattern.class, new PatternAnnotationProcessor());
    }
    private static void addValidator(Class clazz, ValidationAnnotationProcessor vap) {
        annotationProcessors.put(clazz, vap);
    }

    public static boolean isAnnotationForValidation(Annotation annotation) {
        return annotationProcessors.containsKey(annotation.annotationType());
    }
    private static ValidationAnnotationProcessor getValidator(Annotation annotation) {
        return annotationProcessors.get(annotation.annotationType());
    }
    private static boolean appendType(boolean hasType, ModelWriter sb, String type) throws IOException {
        if( !hasType ) {
            sb.write( "type: \"" ).write( type ).writeLine( "\"," );
        }
        return true;
    }

    public void writeValidation( List<Annotation> validationAnnotations, final ModelWriter writer ) throws IOException {
        ItemProcessor<Annotation> annotationItemProcessor = new ItemProcessor<Annotation>() {
            boolean hasPropertyTypeSet = false;
            @Override
            protected void process( Annotation annotation, boolean isLast ) throws IOException {
                ValidationAnnotationProcessor annotationProcessor = getValidator(annotation);
                if( annotationProcessor != null ) {
                    if( annotationProcessor.requiresType() ) {
                        hasPropertyTypeSet = appendType(hasPropertyTypeSet, writer, annotationProcessor.requiredType() );
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
}
