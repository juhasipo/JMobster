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
import fi.vincit.jmobster.processor.ModelWriter;
import fi.vincit.jmobster.processor.frameworks.backbone.type.FieldTypeConverterManager;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.BackboneValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.ItemHandler;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

public class BackboneModelWriter implements ModelWriter {

    private JavaScriptWriter writer;
    final private BackboneValidatorWriterManager validatorWriterManager;
    final private FieldValueConverter valueConverter;

    public BackboneModelWriter( DataWriter writer, FieldValueConverter valueConverter, FieldTypeConverterManager typeConverterManager ) {
        this.writer = new JavaScriptWriter(writer);
        this.validatorWriterManager = new BackboneValidatorWriterManager(this.writer);
        this.valueConverter = valueConverter;
    }

    private static final String DEFAULTS_BLOCK_NAME = "defaults";
    private static final String RETURN_BLOCK = "return "; // Note the space
    private static final String VALIDATOR_BLOCK_NAME = "validation";

    @Override
    public void write( Model model ) {
        writeValidators( model );
        writeFields( model );
    }

    private void writeValidators( Model model ) {
        writer.writeKey( VALIDATOR_BLOCK_NAME ).startBlock();
        final ItemHandler<Validator> validatorWriter = new ItemHandler<Validator>() {
            @Override
            public void process( Validator validator, ItemStatus status ) {
                validatorWriterManager.write( validator, status.isLastItem() );
            }
        };

        ItemProcessor.process(model.getFields()).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                writer.writeKey(field.getName()).startBlock();
                ItemProcessor.process(validatorWriter, field.getValidators());
                writer.endBlock( status.isLastItem() );
            }
        });
        writer.endBlock(false);
    }

    private void writeFields( Model model ) {
        writer.writeKey(DEFAULTS_BLOCK_NAME).startAnonFunction();
        writer.write(RETURN_BLOCK).startBlock();
        ItemProcessor.process( model.getFields() ).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                String defaultValue = valueConverter.convert(field.getFieldType(), null);
                writer.writeKeyValue(field.getName(), defaultValue, status.isLastItem());
            }
        });
        writer.endBlock();
        writer.endBlock(!model.hasValidations());
    }

    public void setWriter( DataWriter writer ) {
        this.writer = new JavaScriptWriter(writer);
        this.validatorWriterManager.setWriter(this.writer);
    }
}
