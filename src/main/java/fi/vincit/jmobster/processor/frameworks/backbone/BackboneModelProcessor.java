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
import fi.vincit.jmobster.processor.frameworks.base.BaseFieldTypeConverterManager;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
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
public class BackboneModelProcessor extends BaseModelProcessor<JavaScriptWriter> {
    private static final Logger LOG = LoggerFactory.getLogger( BackboneModelProcessor.class );

    private static final String NAMESPACE_START = "{";


    private static final String VARIABLE = "var";
    private static final String NAMESPACE_END = "};";
    private static final String DEFAULT_START_COMMENT = "/*\n * Auto-generated file\n */";
    private static final String DEFAULT_NAMESPACE = "Models";

    private static final String DEFAULTS_BLOCK_NAME = "defaults";
    private static final String VALIDATOR_BLOCK_NAME = "validation";

    private static final String MODEL_EXTEND_START = ": Backbone.Model.extend({";
    private static final String MODEL_EXTEND_END = "})";

    private String startComment;
    private String namespaceName;

    private ValidatorProcessor validatorProcessor;
    private ModelProcessor<JavaScriptWriter> defaultValueProcessor;

    /**
     * Construct slightly customized model processor with custom writer, naming strategy and annotation writer.
     * @param writer Writer
     */
    public BackboneModelProcessor(DataWriter writer,
                                  FieldValueConverter valueConverter) {
        super(new JavaScriptWriter(writer), valueConverter);
        initRest(
                new ValidatorProcessor(getWriter(), valueConverter, new BackboneValidatorWriterManager( getWriter() )),
                new DefaultValueProcessor(getWriter(), valueConverter)
        );
    }

    public BackboneModelProcessor(DataWriter writer,
                                  FieldValueConverter valueConverter,
                                  ValidatorProcessor validatorProcessor,
                                  ModelProcessor<JavaScriptWriter> valueProcessor) {
        super(new JavaScriptWriter(writer), valueConverter);
        initRest(
                validatorProcessor,
                valueProcessor
        );
    }

    private void initRest(ValidatorProcessor validatorProcessor,
                          ModelProcessor<JavaScriptWriter> valueProcessor) {
        this.startComment = DEFAULT_START_COMMENT;
        this.namespaceName = DEFAULT_NAMESPACE;
        this.validatorProcessor = validatorProcessor;
        this.defaultValueProcessor = valueProcessor;
    }

    @Override
    public void startProcessing(ItemStatus status) throws IOException {
        LOG.trace( "Starting to process models" );
        getWriter().writeLine( startComment );
        getWriter().writeLine( VARIABLE + " " + namespaceName + " = " + NAMESPACE_START );
        getWriter().indent();
    }

    @Override
    public void processModel( Model model, ItemStatus status ) {
        LOG.trace("Processing model: {}", model.toString());
        String modelName = model.getName();

        getWriter().write( modelName ).writeLine( MODEL_EXTEND_START ).indent();

        writeSection(VALIDATOR_BLOCK_NAME, model, validatorProcessor, ItemStatuses.first());
        writeSection(DEFAULTS_BLOCK_NAME, model, defaultValueProcessor, ItemStatuses.last());

        getWriter().indentBack();
        getWriter().writeLine( MODEL_EXTEND_END, ",", status.isNotLastItem() );
    }

    private void writeSection(String sectionName, Model model, ModelProcessor processor, ItemStatus position) {
        try {
            getWriter().writeKey( sectionName );
            processor.startProcessing(position);
            processor.processModel(model, ItemStatuses.firstAndLast());
            processor.endProcessing(position);
        } catch( IOException e ) {
            LOG.error("Error while processing section "+sectionName, e);
        }
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing(ItemStatus status) throws IOException {
        getWriter().indentBack();
        getWriter().writeLine(NAMESPACE_END);
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
