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
import fi.vincit.jmobster.processor.defaults.base.BaseModelProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.validator.writer.BackboneValidatorWriterManager;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.itemprocessor.ItemHandler;
import fi.vincit.jmobster.util.itemprocessor.ItemProcessor;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.io.IOException;

/**
 * Implements validation part of a Backbone.validation model
 * Can be used as a separate ModelProcessor.
 */
public class ValidatorProcessor extends BaseModelProcessor<JavaScriptWriter> {

    private final BackboneValidatorWriterManager validatorWriterManager;
    private final ItemHandler<Validator> validatorWriter;

    /**
     * Construct slightly customized model processor with custom writer, naming strategy and annotation writer.
     * @param writer Writer
     * @param validatorWriterManager
     */
    public ValidatorProcessor( String name,
                               JavaScriptWriter writer,
                               FieldValueConverter valueConverter,
                               final BackboneValidatorWriterManager validatorWriterManager ) {
        super(name, writer, valueConverter);
        this.validatorWriterManager = validatorWriterManager;
        validatorWriter = new ItemHandler<Validator>() {
            @Override
            public void process( Validator validator, ItemStatus status ) {
                validatorWriterManager.write( validator, status );
            }
        };
    }

    public ValidatorProcessor( String name,
                               FieldValueConverter valueConverter,
                               final BackboneValidatorWriterManager validatorWriterManager ) {
        super(name, null, valueConverter);
        this.validatorWriterManager = validatorWriterManager;
        validatorWriter = new ItemHandler<Validator>() {
            @Override
            public void process( Validator validator, ItemStatus status ) {
                validatorWriterManager.write( validator, status );
            }
        };
    }

    public ValidatorProcessor( String name,
                               DataWriter writer,
                               FieldValueConverter valueConverter,
                               BackboneValidatorWriterManager validatorWriterManager ) {
        this(name, new JavaScriptWriter(writer), valueConverter, validatorWriterManager);
    }

    @Override
    public void startProcessing(ItemStatus status) throws IOException {
        getWriter().startBlock();
    }

    @Override
    public void processModel( Model model, ItemStatus status ) {
        ItemProcessor.process( model.getFields() ).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                getWriter().writeKey( field.getName() ).startBlock();
                ItemProcessor.process(validatorWriter, field.getValidators());
                getWriter().endBlock( status );
            }
        });
    }

    @Override
    public void endProcessing(ItemStatus status) throws IOException {
        getWriter().endBlock( status );
    }
}
