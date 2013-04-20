package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * <p>
 *      Class used to create a client side model from server side Java entity/DTO/POJO.
 * </p>
 * <p>
 *     If uncaught IOExceptions are caught, the model processing will be terminated.
 *  {@link fi.vincit.jmobster.processor.ModelProcessor#endProcessing(ItemStatus)}
 *  is not called in those cases (of unless the exception was thrown from that method).
 *  The error will be logged (level: Error).
 * </p>
 */
public class DefaultModelGenerator implements ModelGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger( DefaultModelGenerator.class );

    private final ModelProcessor modelProcessor;

    /**
     * Creates new DefaultModelGenerator
     * @param modelProcessor Model processor to use
     */
    public DefaultModelGenerator(ModelProcessor modelProcessor) {
        this.modelProcessor = modelProcessor;
    }

    @Override
    public void processAll( Collection<Model> models ) {
        processModelsInternal( models );
    }

    @Override
    public void process( Model model ) {
        processModelsInternal(Arrays.asList(model));
    }

    @Override
    public void setWriter( DataWriter dataWriter ) {
        modelProcessor.setWriter( dataWriter );
    }

    /**
     * Process the given models. If IOException is caught in this method
     * the processing will be terminated as described in the class documentation.
     * @param models Models to process
     */
    private void processModelsInternal( Collection<Model> models ) {
        try {
            modelProcessor.startProcessing(ItemStatuses.firstAndLast());
            ItemProcessor.process(models).with(new ItemHandler<Model>() {
                @Override
                public void process( Model model, ItemStatus status ) {
                    modelProcessor.processModel( model, status );
                }
            });
            modelProcessor.endProcessing(ItemStatuses.firstAndLast());
        } catch (IOException e) {
            LOG.error("Error", e);
        }
    }

}
