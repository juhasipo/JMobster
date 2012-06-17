package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.annotation.IgnoreDefaultValue;
import fi.vincit.modelgenerator.backbone.DefaultAnnotationProcessorProvider;
import fi.vincit.modelgenerator.converter.JavaToJSValueConverter;
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

    public ModelProcessor modelProcessor;
    private FieldScanner fieldScanner;


    public ModelGenerator( ModelProcessor modelProcessor, JavaToJSValueConverter fieldDefaultValueProcessor ) {
        this.modelProcessor = modelProcessor;
        this.fieldScanner = new FieldScanner(fieldDefaultValueProcessor, new DefaultAnnotationProcessorProvider());
    }

    public void process(Class... classes) {
        List<Model> models = getModels( classes );
        LOG.debug("Found {} models", models.size());

        try {
            LOG.debug("Start processing models");
            modelProcessor.startProcessing();
            for( int i = 0; i < models.size(); ++i ) {
                Model model = models.get( i );
                boolean isLastModel = i == models.size() - 1;
                modelProcessor.processModel( model, isLastModel );
            }
            modelProcessor.endProcessing();
        } catch (IOException e) {
            LOG.error("Error", e);
        }
    }

    private List<Model> getModels( Class[] classes ) {
        List<Model> models = new ArrayList<Model>();
        for( Class clazz : classes ) {
            List<ModelField> modelFields = fieldScanner.getFields(clazz);
            LOG.debug("Added {} model fields for class {}", modelFields.size(), clazz.getSimpleName());
            Model model = new Model(clazz, modelFields);
            models.add( model );
            checkAndSetValidationState( model );
        }
        return models;
    }

    private void checkAndSetValidationState( Model model ) {
        for( ModelField modelField : model.getFields() ) {
            if( modelField.hasValidations() ) {
                model.setValidations(true);
                break;
            }
        }
    }

}
