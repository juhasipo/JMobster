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
import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.BackboneValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
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
public class BackboneModelProcessor extends BaseModelProcessor<JavaScriptWriter> {

    public static final String NAME = "";

    private static final Logger LOG = LoggerFactory.getLogger( BackboneModelProcessor.class );

    private static final String BLOCK_START = "{";
    private static final String NAMESPACE_START =BLOCK_START;


    private static final String VARIABLE = "var";
    private static final String BLOCK_END = "}";
    private static final String NAMESPACE_END = "};";
    private static final String DEFAULT_START_COMMENT = "/*\n * Auto-generated file\n */";
    private static final String DEFAULT_NAMESPACE = "Models";

    private static final String DEFAULTS_BLOCK_NAME = "defaults";
    private static final String VALIDATOR_BLOCK_NAME = "validation";

    private static final String MODEL_EXTEND_START = ": Backbone.Model.extend({";
    private static final String MODEL_EXTEND_END = "})";

    private String startComment;
    private String namespaceName;

    private BackboneModelProcessor(Builder builder) {
        super(NAME);

        for( ModelProcessor<JavaScriptWriter> processor : builder.modelProcessors ) {
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

    private OutputMode getOutputMode() {
        return ((JavaScriptContext)this.getContext()).getOutputMode();
    }

    public static class Builder {
        private JavaScriptContext context;
        private FieldValueConverter valueConverter;
        private ModelProcessor<JavaScriptWriter>[] modelProcessors =
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

        public Builder setModelProcessors(ModelProcessor<JavaScriptWriter>... modelProcessors) {
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
        if( getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().writeLine( startComment );
            getWriter().writeLine(VARIABLE + " " + namespaceName + " = " + NAMESPACE_START);
        } else {
            getWriter().writeLine( NAMESPACE_START );
        }
        getWriter().indent();
    }

    @Override
    public void processModel( final Model model, ItemStatus status ) {
        LOG.trace("Processing model: {}", model.toString());
        String modelName = model.getName();
        if( getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().write( modelName ).writeLine( MODEL_EXTEND_START ).indent();
        } else {
            getWriter().writeKey( modelName ).writeLine(BLOCK_START).indent();
        }
        ItemProcessor
                .process(getModelProcessors())
                .with(new ItemHandler<ModelProcessor<JavaScriptWriter>>() {
                    @Override
                    public void process(ModelProcessor<JavaScriptWriter> item, ItemStatus status) {
                        writeSection(item.getName(), model, item, status);
                    }
                });

        getWriter().indentBack();
        if( getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().writeLine( MODEL_EXTEND_END, ",", status.isNotLastItem() );
        } else {
            getWriter().writeLine( BLOCK_END, ",", status.isNotLastItem() );
        }
    }

    private void writeSection(String sectionName, Model model, ModelProcessor processor, ItemStatus position) {
        try {
            getWriter().writeKey( sectionName );
            processor.startProcessing(position);
            processor.processModel(model, position);
            processor.endProcessing(position);
        } catch( IOException e ) {
            LOG.error("Error while processing section "+sectionName, e);
        }
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing(ItemStatus status) throws IOException {
        getWriter().indentBack();
        if( getOutputMode() == OutputMode.JAVASCRIPT) {
            getWriter().writeLine(NAMESPACE_END);
        } else {
            getWriter().writeLine(BLOCK_END);
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
