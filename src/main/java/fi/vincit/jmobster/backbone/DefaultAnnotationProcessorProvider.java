package fi.vincit.jmobster.backbone;

import fi.vincit.jmobster.ValidationAnnotationProcessor;
import fi.vincit.jmobster.backbone.annotation.*;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * The class will return a suitable annotation processor for the
 * given type of JSR-303 validation annotation. The class can
 * be extended to support custom validation annotation. For now
 * only single property annotations are supported.
 */
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

    protected static void addValidator( Class clazz, ValidationAnnotationProcessor vap ) {
        annotationProcessors.put(clazz, vap);
    }

    @Override
    public boolean isAnnotationForValidation(Annotation annotation) {
        return annotationProcessors.containsKey(annotation.annotationType());
    }

    /**
     * Returns the best annotation processor for the given annotation type.
     * @param annotation
     * @return Annotation processor if found, otherwise null.
     */
    @Override
    public ValidationAnnotationProcessor getValidator( Annotation annotation ) {
        return annotationProcessors.get(annotation.annotationType());
    }
}
