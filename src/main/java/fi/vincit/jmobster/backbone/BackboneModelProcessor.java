package fi.vincit.jmobster.backbone;
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

import fi.vincit.jmobster.processor.DefaultNamingStrategy;
import fi.vincit.jmobster.processor.Model;
import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.ModelProcessor;
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

    private ModelWriter writer;
    private String modelFilePath;
    private AnnotationProcessor annotationProcessor;
    private ModelNamingStrategy modelNamingStrategy;

    private String startComment;
    private String namespaceName;

    public BackboneModelProcessor( String modelFilePath ) {
        this.modelFilePath = modelFilePath;
        annotationProcessor = new DefaultAnnotationProcessor(new DefaultAnnotationProcessorProvider());
        modelNamingStrategy = new DefaultNamingStrategy();
        startComment = "/*\n * Auto-generated file\n */";
        namespaceName = "Models";
    }

    public BackboneModelProcessor( ModelWriter writer ) {
        this( (String)null );
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

        DefaultValueSectionWriter defaultValueSectionWriter = new DefaultValueSectionWriter(writer);
        defaultValueSectionWriter.writeDefaultValues( model.getFields(), model.hasValidations() );

        ValidationSectionWriter validationSectionWriter =
                new ValidationSectionWriter(writer, annotationProcessor);
        validationSectionWriter.writeValidators( model.getFields() );

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
