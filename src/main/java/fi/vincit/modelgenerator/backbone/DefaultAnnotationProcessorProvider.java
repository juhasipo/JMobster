package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.ValidationAnnotationProcessor;
import fi.vincit.modelgenerator.backbone.annotation.*;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class DefaultAnnotationProcessorProvider implements AnnotationProcessorProvider {
    private static Map<Class, ValidationAnnotationProcessor> annotationProcessors;
    static {
        annotationProcessors = new HashMap<Class, ValidationAnnotationProcessor>();
        addValidator( NotNull.class, new NotNullAnnotationProcessor() );
        addValidator( Size.class, new SizeAnnotationProcessor() );
        addValidator( Min.class, new MinAnnotationProcessor() );
        addValidator( Max.class, new MaxAnnotationProcessor() );
        addValidator( Pattern.class, new PatternAnnotationProcessor() );
    }

    private static void addValidator( Class clazz, ValidationAnnotationProcessor vap ) {
        annotationProcessors.put(clazz, vap);
    }

    @Override
    public boolean isAnnotationForValidation(Annotation annotation) {
        return annotationProcessors.containsKey(annotation.annotationType());
    }

    @Override
    public ValidationAnnotationProcessor getValidator( Annotation annotation ) {
        return annotationProcessors.get(annotation.annotationType());
    }
}
