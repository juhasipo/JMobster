package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.annotation.IgnoreDefaultValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModelGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger( ModelGenerator.class );

    private List<Model> models;

    public ModelProcessor modelProcessor;
    public FieldDefaultValueProcessor fieldDefaultValueProcessor;

    public ModelGenerator( ModelProcessor modelProcessor, FieldDefaultValueProcessor fieldDefaultValueProcessor ) {
        this.modelProcessor = modelProcessor;
        this.fieldDefaultValueProcessor = fieldDefaultValueProcessor;
    }

    public void process(Class... classes) {
        models = getModels(classes);

        try {
            modelProcessor.startProcessing();
            for( int i = 0; i < models.size(); ++i ) {
                Model model = models.get(i);
                boolean isLastModel = i == models.size() - 1;
                modelProcessor.processModel( model, isLastModel );
            }
            modelProcessor.endProcessing();
        } catch (IOException e) {

        }
    }

    private List<Model> getModels( Class[] classes ) {
        List<Model> models = new ArrayList<Model>();
        for( Class clazz : classes ) {
            Model m = new Model(clazz, getFields( clazz ));
            models.add( m );
            for( ModelField mf : m.getFields() ) {
                if( mf.hasValidations() ) {
                    m.setValidations(true);
                    break;
                }
            }
        }
        return models;
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
        Class type = annotation.annotationType();
        return type.equals(Size.class) || type.equals(NotNull.class) ||
                type.equals(Min.class) || type.equals(Max.class) ||
                type.equals(Pattern.class);

    }

    private boolean shouldAddField(Field field) {
        return !field.isAnnotationPresent( IgnoreDefaultValue.class);
    }
}
