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

import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.ItemProcessor;

import java.util.List;

/**
 * Class writes the Backbone.js defaults section.
 * Internally uses {@link JavaScriptWriter}. Intended use
 * is that this class is used before {@link BackboneValidationSectionWriter}
 * and this class will handle the comma between the two sections.
 */
public class BackboneValueSectionWriter {
    private static final String DEFAULTS_BLOCK_NAME = "defaults";
    private static final String RETURN_BLOCK = "return "; // Note the space

    private JavaScriptWriter writer;

    /**
     * Constructs section writer without default model writer.
     * Writer must be set before calling {@link BackboneValueSectionWriter#writeDefaultValues(java.util.List, boolean)}.
     */
    public BackboneValueSectionWriter() {
    }

    /**
     * Constructs section writer with default writer
     * @param writer Model writer
     */
    public BackboneValueSectionWriter( ModelWriter writer ) {
        setWriter(writer);
    }

    /**
     * Sets default model writer
     * @param writer Model writer
     */
    public void setWriter( ModelWriter writer ) {
        this.writer = new JavaScriptWriter(writer);
    }

    /**
     * Write default values to model
     * @param fields Model fields
     * @param hasValidators Set to true if the model contains validations.
     */
    public void writeDefaultValues( List<ModelField> fields, boolean hasValidators ) {
        writer.writeKey(DEFAULTS_BLOCK_NAME).startAnonFunction();
        writer.write(RETURN_BLOCK).startBlock();

        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) {
                writer.writeKeyValue(field.getName(), field.getDefaultValue(), isLastItem);
            }
        };
        modelFieldItemProcessor.process(fields);

        writer.endBlock();
        writer.endBlock(!hasValidators);
    }
}
