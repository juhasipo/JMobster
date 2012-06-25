package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.util.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to create a client side model from
 * server side Java entity/DTO/POJO.
 */
public class DefaultModelGenerator implements ModelGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultModelGenerator.class );

    public ModelProcessor modelProcessor;
    private FieldScanner fieldScanner;


    public DefaultModelGenerator(
            ModelProcessor modelProcessor,
            FieldValueConverter fieldDefaultValueProcessor,
            AnnotationProcessorProvider annotationProcessorProvider) {
        this.modelProcessor = modelProcessor;
        this.fieldScanner = new FieldScanner(fieldDefaultValueProcessor, annotationProcessorProvider);
    }

    @Override
    public void process( Class... classes ) {
        List<Model> models = getModels( classes );
        try {
            modelProcessor.startProcessing();
            ItemProcessor<Model> modelItemProcessor = new ItemProcessor<Model>() {
                @Override
                protected void process( Model model, boolean isLastModel ) {
                    modelProcessor.processModel( model, isLastModel );
                }
            };
            modelItemProcessor.process(models);

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
            Model model = new Model(clazz, modelFields);
            models.add( model );
            checkAndSetValidationState( model );
        }
        return models;
    }

    /**
     * Checks and sets the validation state for the given model.
     * If the given model has at least one field that requires
     * validation the model property validations will be set
     * to true. If the model doesn't have any validation
     * requirements the property will be set to false.
     * @param model Model to check
     */
    private void checkAndSetValidationState( Model model ) {
        for( ModelField modelField : model.getFields() ) {
            if( modelField.hasValidations() ) {
                model.setValidations(true);
                return;
            }
        }
        model.setValidations(false);
    }

    @Override
    public ModelProcessor getModelProcessor() {
        return modelProcessor;
    }
}
