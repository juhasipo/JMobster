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

import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.defaults.DefaultAnnotationProcessor;
import fi.vincit.jmobster.processor.defaults.DefaultNamingStrategy;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.StreamModelWriter;
import fi.vincit.jmobster.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>
 *     Backbone.js implementation of ModelProcessor. It will put
 *     all the given models inside a single namespace called "Models".
 * </p>
 */
public class BackboneModelProcessor implements ModelProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( BackboneModelProcessor.class );
    private static final String NAMESPACE_START = "{";
    private static final String MODEL_EXTEND_START = ": Backbone.Model.extend({";
    private static final String MODEL_EXTEND_END = "})";
    private static final String VARIABLE = "var";
    private static final String NAMESPACE_END = "};";
    private static final String DEFAULT_START_COMMENT = "/*\n * Auto-generated file\n */";
    private static final String DEFAULT_NAMESPACE = "Models";

    private ModelWriter writer;
    private String modelFilePath;
    private AnnotationProcessor annotationProcessor;
    private ModelNamingStrategy modelNamingStrategy;

    private String startComment;
    private String namespaceName;

    private BackboneValueSectionWriter backboneValueSectionWriter;
    private BackboneValidationSectionWriter backboneValidationSectionWriter;

    /**
     * Constructs backbone model processor with a model writer that writes to the given file.
     * @param modelFilePath File path
     * @param groupMode Group mode
     * @param fieldAnnotationWriter Field annotation writer
     */
    public BackboneModelProcessor( String modelFilePath, GroupMode groupMode, FieldAnnotationWriter fieldAnnotationWriter ) {
        this.modelFilePath = modelFilePath;
        this.annotationProcessor = new DefaultAnnotationProcessor(fieldAnnotationWriter, groupMode);
        this.modelNamingStrategy = new DefaultNamingStrategy();
        this.startComment = DEFAULT_START_COMMENT;
        this.namespaceName = DEFAULT_NAMESPACE;
        this.backboneValueSectionWriter = new BackboneValueSectionWriter();
        this.backboneValidationSectionWriter = new BackboneValidationSectionWriter(this.annotationProcessor);
    }


    public BackboneModelProcessor(ModelWriter writer, ModelNamingStrategy namingStrategy, GroupMode groupMode, FieldAnnotationWriter fieldAnnotationWriter) {
        this.writer = writer;
        this.modelNamingStrategy = namingStrategy;
        this.annotationProcessor = new DefaultAnnotationProcessor(fieldAnnotationWriter, groupMode);
        this.startComment = DEFAULT_START_COMMENT;
        this.namespaceName = DEFAULT_NAMESPACE;
        this.backboneValueSectionWriter = new BackboneValueSectionWriter();
        this.backboneValidationSectionWriter = new BackboneValidationSectionWriter(this.annotationProcessor);

    }

    /**
     * Constructs fully customized model processor.
     * @param writer Model writer to use
     * @param annotationProcessor Annotation processor
     * @param modelNamingStrategy Model naming strategy
     * @param startComment Start command
     * @param namespaceName Namespace name
     * @param backboneValueSectionWriter Backbone value section writer
     * @param backboneValidationSectionWriter Backbone validation section writer
     */
    public BackboneModelProcessor(
            ModelWriter writer,
            AnnotationProcessor annotationProcessor,
            ModelNamingStrategy modelNamingStrategy,
            String startComment,
            String namespaceName,
            BackboneValueSectionWriter backboneValueSectionWriter,
            BackboneValidationSectionWriter backboneValidationSectionWriter ) {
        this.writer = writer;
        this.annotationProcessor = annotationProcessor;
        this.modelNamingStrategy = modelNamingStrategy;
        this.startComment = startComment;
        this.namespaceName = namespaceName;
        this.backboneValueSectionWriter = backboneValueSectionWriter;
        this.backboneValidationSectionWriter = backboneValidationSectionWriter;
    }

    public BackboneModelProcessor( ModelWriter writer, FieldAnnotationWriter fieldAnnotationWriter ) {
        this((String)null, GroupMode.ANY_OF_REQUIRED, fieldAnnotationWriter );
        this.writer = writer;
    }

    @Override
    public void startProcessing() throws IOException {
        if( writer == null ) {
            writer = new StreamModelWriter(modelFilePath);
        }
        writer.open();

        writer.writeLine( startComment );
        writer.writeLine(VARIABLE + " " + namespaceName + " = " + NAMESPACE_START);
        writer.indent();
    }

    @Override
    public void processModel( Model model, boolean isLastModel ) {
        String modelName = modelNamingStrategy.getName(model);

        writer.write(modelName).writeLine( MODEL_EXTEND_START ).indent();

        backboneValueSectionWriter.setWriter(writer);
        backboneValueSectionWriter.writeDefaultValues( model.getFields(), model.hasValidations() );

        if( model.hasValidations() ) {
            backboneValidationSectionWriter.setWriter(writer);
            backboneValidationSectionWriter.writeValidators( model.getFields() );
        }
        writer.indentBack();
        writer.writeLine( MODEL_EXTEND_END, ",", !isLastModel);
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing() throws IOException {
        writer.indentBack();
        writer.writeLine( NAMESPACE_END );
        writer.close();
    }

    @Override
    public AnnotationProcessor getAnnotationProcessor() {
        return annotationProcessor;
    }

    public String getModelFilePath() {
        return modelFilePath;
    }

    @Override
    public void setModelNamingStrategy( ModelNamingStrategy modelNamingStrategy ) {
        this.modelNamingStrategy = modelNamingStrategy;
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
