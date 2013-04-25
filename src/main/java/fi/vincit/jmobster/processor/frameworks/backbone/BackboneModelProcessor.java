package fi.vincit.jmobster.processor.frameworks.backbone;
/*
 * Copyright 2012 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.BackboneValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.javascript.BaseJavaScriptModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 *     Backbone.js implementation of ModelProcessor. It will put
 *     all the given models inside a single namespace called "Models".
 * </p>
 */
@SuppressWarnings( "HardcodedFileSeparator" )
public class BackboneModelProcessor extends BaseJavaScriptModelProcessor {

    public static final String NAME = "";

    private static final Logger LOG = LoggerFactory.getLogger( BackboneModelProcessor.class );

    private static final String DEFAULT_START_COMMENT = "Auto-generated file";
    private static final String DEFAULT_NAMESPACE = "Models";

    private static final String DEFAULTS_BLOCK_NAME = "defaults";
    private static final String VALIDATOR_BLOCK_NAME = "validation";

    private static final String BACKBONE_MODEL_EXTEND = "Backbone.Model.extend";

    private String startComment;
    private String namespaceName;

    private BackboneModelProcessor(Builder builder) {
        super(NAME);

        for( ModelProcessor<JavaScriptContext, JavaScriptWriter> processor : builder.modelProcessors ) {
            addModelProcessor(processor);
        }

        setStartComment(DEFAULT_START_COMMENT);
        setNamespaceName(DEFAULT_NAMESPACE);
        setLanguageContext(builder.context);
        setFieldValueConverter(builder.valueConverter);

        if( builder.context.getOutputMode() == OutputMode.JSON ) {
            getWriter().setJSONmode(true);
        }
    }

    public static class Builder {
        private JavaScriptContext context;
        private FieldValueConverter valueConverter;
        private ModelProcessor<JavaScriptContext, JavaScriptWriter>[] modelProcessors =
                new ModelProcessor[0];

        public Builder(DataWriter writer, OutputMode outputMode) {
            context = new JavaScriptContext(
                    new JavaScriptWriter(writer),
                    outputMode
            );
        }

        public Builder(JavaScriptContext languageContext) {
            context = languageContext;
        }

        public Builder(JavaScriptWriter writer, OutputMode outputMode) {
            context = new JavaScriptContext(writer, outputMode);
        }

        public Builder setValueConverter(FieldValueConverter valueConverter) {
            this.valueConverter = valueConverter;
            return this;
        }

        public Builder setModelProcessors(ModelProcessor<JavaScriptContext, JavaScriptWriter>... modelProcessors) {
            this.modelProcessors = modelProcessors;
            return this;
        }

        public Builder useDefaultModelProcessors() {
            setModelProcessors(
                    new ValidatorProcessor.Builder()
                            .setName(VALIDATOR_BLOCK_NAME)
                            .setValidatorWriterManager(new BackboneValidatorWriterManager())
                            .build(),
                    new DefaultValueProcessor.Builder()
                            .setName(DEFAULTS_BLOCK_NAME)
                            .build()
            );
            return this;
        }

        public BackboneModelProcessor build() {
            return new BackboneModelProcessor(this);
        }
    }

    @Override
    public void startProcessing(ItemStatus status) throws IOException {
        LOG.trace( "Starting to process models" );
        if( getContext().getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().writeComment(startComment);
            getWriter().writeVariable(namespaceName, "", JavaScriptWriter.VariableType.BLOCK);
        } else {
            getWriter().startBlock();
        }
        getWriter().indent();
    }

    @Override
    public void processModel( final Model model, ItemStatus status ) {
        LOG.trace("Processing model: {}", model.toString());
        String modelName = model.getName();
        if( getContext().getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().write(modelName).writeKey("").startFunctionCallBlock(BACKBONE_MODEL_EXTEND);
        } else {
            getWriter().writeKey( modelName ).startBlock();
        }
        ItemProcessor
                .process(getModelProcessors())
                .with(new ItemHandler<ModelProcessor<JavaScriptContext, JavaScriptWriter>>() {
                    @Override
                    public void process(ModelProcessor<JavaScriptContext, JavaScriptWriter> item, ItemStatus status) {
                        writeSection(item.getName(), model, item, status);
                    }
                });

        getWriter().indentBack();
        if( getContext().getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().endFunctionCallBlock(status);
        } else {
            getWriter().endBlock(status);
        }
    }

    private void writeSection(String sectionName, Model model, ModelProcessor processor, ItemStatus position) {
        try {
            getWriter().writeKey( sectionName );
            processor.doStartProcessing(position);
            processor.doProcessModel(model, position);
            processor.doEndProcessing(position);
        } catch( IOException e ) {
            LOG.error("Error while processing section "+sectionName, e);
        }
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing(ItemStatus status) throws IOException {
        getWriter().indentBack();
        if( getContext().getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().endBlockStatement();
        } else {
            getWriter().endBlock(ItemStatuses.last());
        }
        getWriter().close();
        LOG.trace("Processing models done");
    }

    /**
     * Set start commend for the model file. Note that you have include
     * comment start and end marks and wrap the comment appropriately.
     * @param startComment Start comment
     */
    public void setStartComment( String startComment ) {
        this.startComment = startComment;
    }

    /**
     * Set namespace in which the models are created
     * @param namespaceName Namespace
     */
    public void setNamespaceName( String namespaceName ) {
        this.namespaceName = namespaceName;
    }
}
