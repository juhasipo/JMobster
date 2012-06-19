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

import fi.vincit.jmobster.processor.ModelField;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.ItemProcessor;

import java.util.List;

/**
 * Class writes the Backbone.js defaults section.
 */
public class DefaultValueSectionWriter {
    private static final String DEFAULT_SECTION_START = "defaults: function() {";
    private static final String DEFAULT_RETURN_START = "return {";
    private static final String MODEL_NAME_ASSIGN = ": ";
    private static final String DEFAULT_RETURN_END = "}";
    private static final String DEFAULT_SECTION_END = "}";
    private ModelWriter writer;

    public DefaultValueSectionWriter( ModelWriter writer ) {
        this.writer = writer;
    }

    /**
     * Write default values to model
     * @param fields Model fields
     * @param hasValidators Set to true if the model contains validations.
     */
    public void writeDefaultValues( List<ModelField> fields, boolean hasValidators ) {
        writer.writeLine( DEFAULT_SECTION_START ).indent();
        writer.writeLine( DEFAULT_RETURN_START ).indent();
        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) {
                writer.write(field.getField().getName()).write( MODEL_NAME_ASSIGN ).write( field.getDefaultValue() );
                writer.writeLine("", ",", !isLastItem);
            }
        };
        modelFieldItemProcessor.process( fields );
        writer.indentBack();
        writer.writeLine( DEFAULT_RETURN_END ).indentBack();
        writer.writeLine( DEFAULT_SECTION_END, ",", hasValidators);
    }
}
