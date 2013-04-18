package fi.vincit.jmobster.processor.frameworks.html5;

import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.languages.html5.writer.HTML5Writer;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;

import java.io.IOException;

public class HTML5FormProcessor extends BaseModelProcessor<HTML5Writer> {

    private ModelConfiguration configuration;
    private BaseModelProcessor<HTML5Writer> fieldProcessor;

    public HTML5FormProcessor() {
        super("");
    }

    @Override
    public void startProcessing(ItemStatus status) throws IOException {
    }

    @Override
    public void processModel(final Model model, ItemStatus status) {
        if( fieldProcessor != null ) {
            fieldProcessor.processModel(model, status);
        }
        ItemProcessor
            .process(model.getFields())
            .with(new ItemHandler<ModelField>() {
                @Override
                public void process(ModelField item, ItemStatus status) {
                    if( hasConfiguration(model.getModelClass(), item.getName()) ) {
                        ModelConfiguration.ModelFieldConfigurationImmutable mc =
                                configuration.getModelFieldConfiguration(model.getModelClass(), item.getName());
                        String type;
                        if( mc.useDefaultType() ) {
                            type = "";
                        } else {
                            type = mc.getType();
                        }
                        getWriter().writeInput(type, mc.getName(), mc.getName(), HTML5Writer.NO_VALUE);
                    } else {
                        getWriter().writeInput("text", item.getName(), item.getName(), HTML5Writer.NO_VALUE);
                    }
                }
            });
    }

    private boolean hasConfiguration(Class model, String fieldName) {
        return configuration != null && configuration.hasConfiguration(model, fieldName);
    }

    @Override
    public void endProcessing(ItemStatus status) throws IOException {
    }

    public void setConfiguration(ModelConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setFieldProcessor(BaseModelProcessor<HTML5Writer> fieldProcessor) {
        this.fieldProcessor = fieldProcessor;
    }
}
