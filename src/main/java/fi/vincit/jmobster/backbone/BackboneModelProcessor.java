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

    private ModelWriter writer;
    private String modelFilePath;
    private AnnotationProcessor annotationProcessor;
    private ModelNamingStrategy modelNamingStrategy;

    public BackboneModelProcessor( String modelFilePath ) {
        this.modelFilePath = modelFilePath;
        annotationProcessor = new DefaultAnnotationProcessor(new DefaultAnnotationProcessorProvider());
        modelNamingStrategy = new DefaultNamingStrategy();
    }

    public BackboneModelProcessor( ModelWriter writer ) {
        this((String)null);
        this.writer = writer;
    }

    @Override
    public void startProcessing() throws IOException {
        if( writer == null ) {
            writer = new StreamModelWriter(modelFilePath);
        }
        writer.open();

        writer.writeLine("/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Models = {");
        writer.indent();
    }

    @Override
    public void processModel( Model model, boolean isLastModel ) {
        String modelName = modelNamingStrategy.getName(model);

        writer.write(modelName).writeLine( ": Backbone.Model.extend({" ).indent();

        DefaultValueSectionWriter defaultValueSectionWriter = new DefaultValueSectionWriter(writer);
        defaultValueSectionWriter.writeDefaultValues( model.getFields(), model.hasValidations() );

        ValidationSectionWriter validationSectionWriter =
                new ValidationSectionWriter(writer, annotationProcessor);
        validationSectionWriter.writeValidators( model.getFields() );

        writer.indentBack();
        writer.writeLine("})", ",", !isLastModel);
    }

    @Override
    @SuppressWarnings( "RedundantThrows" )
    public void endProcessing() throws IOException {
        writer.indentBack();
        writer.writeLine("};");
        writer.close();
    }

    @Override
    public AnnotationProcessor getAnnotationProcessor() {
        return annotationProcessor;
    }

    public String getModelFilePath() {
        return modelFilePath;
    }
}
