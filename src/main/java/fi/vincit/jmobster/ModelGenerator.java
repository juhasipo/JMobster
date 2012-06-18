package fi.vincit.jmobster;

import fi.vincit.jmobster.backbone.DefaultAnnotationProcessorProvider;
import fi.vincit.jmobster.converter.JavaToJSValueConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to create a client side model from
 * server side Java entity/DTO/POJO.
 */
public class ModelGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger( ModelGenerator.class );

    public ModelProcessor modelProcessor;
    private FieldScanner fieldScanner;


    public ModelGenerator( ModelProcessor modelProcessor, JavaToJSValueConverter fieldDefaultValueProcessor ) {
        this.modelProcessor = modelProcessor;
        this.fieldScanner = new FieldScanner(fieldDefaultValueProcessor, new DefaultAnnotationProcessorProvider());
    }

    /**
     * Process and create the client side model(s) for
     * the given classes.
     * @param classes One or more classes for which the models should be generated.
     */
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

    /**
     * Get a list of internal model objects from the given classes.
     * @param classes Found model classes.
     * @return List of models. If no models are suitable returns an empty list.
     */
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