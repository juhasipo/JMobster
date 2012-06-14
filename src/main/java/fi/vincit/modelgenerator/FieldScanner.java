package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.annotation.IgnoreDefaultValue;
import fi.vincit.modelgenerator.backbone.AnnotationProcessor;
import fi.vincit.modelgenerator.backbone.AnnotationProcessorProvider;
import fi.vincit.modelgenerator.backbone.DefaultAnnotationProcessorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldScanner {
    private static final Logger LOG = LoggerFactory
            .getLogger( FieldScanner.class );

    private FieldDefaultValueProcessor fieldDefaultValueProcessor;
    private AnnotationProcessorProvider annotationProcessorProvider;

    public FieldScanner( FieldDefaultValueProcessor fieldDefaultValueProcessor, AnnotationProcessorProvider annotationProcessorProvider ) {
        this.fieldDefaultValueProcessor = fieldDefaultValueProcessor;
        this.annotationProcessorProvider = annotationProcessorProvider;
    }

    public List<ModelField> getFields(Class clazz) {
        List<ModelField> fields = new ArrayList<ModelField>();

        try {
            Object defaultObject = clazz.newInstance();
            for( Field field : clazz.getDeclaredFields() ) {
                field.setAccessible(true);
                if( shouldAddField(field) ) {
                    LOG.error("Adding field {} to model fields", field.getName());
                    ModelField modelField = new ModelField(field, getValidationAnnotations(field));
                    modelField.setDefaultValue(fieldDefaultValueProcessor.getDefaultValue(field, defaultObject));
                    fields.add( modelField );
                } else {
                    LOG.error("Field {} not added to model fields", field.getName());
                }
            }
        } catch( InstantiationException e ) {
            LOG.error("Instantiation failed", e);
        } catch( IllegalAccessException e ) {
            LOG.error( "Illegal access", e );
        }

        return fields;
    }

    private List<Annotation> getValidationAnnotations( Field field ) {
        List<Annotation> validationAnnotations = new ArrayList<Annotation>();
        for( Annotation annotation : field.getAnnotations() ) {
            if( isValidationAnnotation(annotation) ) {
                validationAnnotations.add(annotation);
            }
        }
        return validationAnnotations;
    }

    private boolean isValidationAnnotation( Annotation annotation ) {
        return annotationProcessorProvider.isAnnotationForValidation( annotation );

    }

    private boolean shouldAddField(Field field) {
        return !field.isAnnotationPresent( IgnoreDefaultValue.class);
    }
}
