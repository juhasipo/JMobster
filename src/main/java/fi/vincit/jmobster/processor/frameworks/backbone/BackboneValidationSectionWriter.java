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

import fi.vincit.jmobster.processor.AnnotationProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import java.util.List;

/**
 * Writes validators to model. Backbone.Validation implementation.
 * Internally uses {@link JavaScriptWriter} to write the model.
 */
public class BackboneValidationSectionWriter {

    private static final String VALIDATE_KEY_NAME = "validate";

    private JavaScriptWriter writer;
    private AnnotationProcessor annotationProcessor;

    /**
     * Constructor without default writer. Writer must be set
     * before calling {@link BackboneValidationSectionWriter#writeValidators(java.util.List)}.
     * @param annotationProcessor Annotation processor
     */
    public BackboneValidationSectionWriter( AnnotationProcessor annotationProcessor ) {
        this.annotationProcessor = annotationProcessor;
    }

    /**
     * Constructor with default writer.
     * @param writer Model writer
     * @param annotationProcessor Annotation processor
     */
    public BackboneValidationSectionWriter( ModelWriter writer, AnnotationProcessor annotationProcessor ) {
        this.annotationProcessor = annotationProcessor;
        setWriter(writer);
    }

    /**
     * Sets writer to use
     * @param writer Model writer
     */
    public final void setWriter( ModelWriter writer ) {
        this.writer = new JavaScriptWriter(writer);
    }

    /**
     * Writes validators for the given fields.
     * @param fields Fields for which to write validators.
     */
    public void writeValidators( List<ModelField> fields ) {
        writer.writeKey( VALIDATE_KEY_NAME ).startBlock();
        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) {
                writer.writeKey(field.getName()).startBlock();
                annotationProcessor.writeValidation(field, writer);
                writer.endBlock(isLastItem);
            }
        };
        modelFieldItemProcessor.process(fields);
        writer.endBlock();
    }
}
