package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.FieldScanner;
import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ItemHandler;
import fi.vincit.jmobster.util.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *      Class used to create a client side model from
 *  server side Java entity/DTO/POJO.
 * </p>
 * <p>
 *     If uncaught IOExceptions are caught, the
 *  model processing will be terminated. {@link fi.vincit.jmobster.processor.ModelProcessor#endProcessing()}
 *  is not called in those cases (of unless the exception was thrown from that method).
 *  The error will be logged (level: Error).
 * </p>
 */
public class DefaultModelGenerator implements ModelGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultModelGenerator.class );

    private ModelProcessor modelProcessor;
    private FieldScanner fieldScanner;
    private ModelNamingStrategy modelNamingStrategy;

    /**
     * Creates new DefaultModelGenerator
     * @param modelProcessor Model processor to use
     * @param fieldScanner Field scanner to use
     */
    public DefaultModelGenerator(
            ModelProcessor modelProcessor,
            FieldScanner fieldScanner,
            ModelNamingStrategy modelNamingStrategy) {
        this.modelProcessor = modelProcessor;
        this.fieldScanner = fieldScanner;
        this.modelNamingStrategy = modelNamingStrategy;
    }

    @Override
    public void process( Class... classes ) {
        List<Model> models = getModels( classes );
        processModelsInternal( models );
    }

    @Override
    public void process( List<Class> classes ) {
        List<Model> models = getModels(classes);
        processModelsInternal(models);
    }

    /**
     * Process the given models. If IOException is caught in this method
     * the processing will be terminated as described in the class documentation.
     * @param models Models to process
     */
    private void processModelsInternal( List<Model> models ) {
        try {
            modelProcessor.startProcessing();
            ItemProcessor.process(models).with(new ItemHandler<Model>() {
                @Override
                public void process( Model model, boolean isLastModel ) {
                    modelProcessor.processModel( model, isLastModel );
                }
            });
            modelProcessor.endProcessing();
        } catch (IOException e) {
            LOG.error("Error", e);
        }
    }

    /**
     * Get a list of internal model objects from the given classes.
     * @param classes Found model classes as an array.
     * @return List of models. If no models are suitable returns an empty list.
     */
    private List<Model> getModels( Class[] classes ) {
        List<Model> models = new ArrayList<Model>();
        for( Class clazz : classes ) {
            createModelAndAddToList( clazz, models );
        }
        return models;
    }

    /**
     * Get a list of internal model objects from the given classes.
     * @param classes Found model classes as a List.
     * @return List of models. If no models are suitable returns an empty list.
     */
    private List<Model> getModels( List<Class> classes ) {
        List<Model> models = new ArrayList<Model>();
        for( Class clazz : classes ) {
            createModelAndAddToList( clazz, models );
        }
        return models;
    }

    /**
     * Get model for the given class and add it to the given list.
     * @param clazz Class for which model should be created
     * @param models Models list
     */
    private void createModelAndAddToList( Class clazz, List<Model> models ) {
        Model model = new Model(clazz, modelNamingStrategy.getName(clazz), fieldScanner.getFields(clazz));
        models.add( model );
    }

    @Override
    public ModelProcessor getModelProcessor() {
        return modelProcessor;
    }
}
