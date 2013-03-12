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
import fi.vincit.jmobster.processor.defaults.validator.BaseValidatorWriterManager;
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

    private BaseValidatorWriterManager validatorWriterManager;
    private ItemHandler<Validator> validatorWriter;

    public static class Builder {
        private String name;
        private FieldValueConverter valueConverter;
        private DataWriter writer;
        private BaseValidatorWriterManager<JavaScriptWriter> validatorWriterManager;

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setValueConverter(FieldValueConverter valueConverter) {
            this.valueConverter = valueConverter;
            return this;
        }

        public Builder setWriter(DataWriter writer) {
            this.writer = writer;
            return this;
        }

        public Builder setValidatorWriterManager(BaseValidatorWriterManager<JavaScriptWriter> validatorWriterManager) {
            this.validatorWriterManager = validatorWriterManager;
            return this;
        }

        public ValidatorProcessor build() {
            if( name == null ) {
                name = "";
            }
            if( validatorWriterManager == null ) {
                validatorWriterManager = new BackboneValidatorWriterManager();
            }
            return new ValidatorProcessor(this);
        }
    }

    private ValidatorProcessor(Builder builder) {
        super(builder.name);
        this.validatorWriterManager = builder.validatorWriterManager;
        if( builder.writer != null ) {
            setWriter(new JavaScriptWriter(builder.writer));
        }
        setFieldValueConverter(builder.valueConverter);

        validatorWriter = new ItemHandler<Validator>() {
            @Override
            public void process( Validator validator, ItemStatus status ) {
                validatorWriterManager.write(validator, status);
            }
        };
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

    @Override
    public void setWriter(JavaScriptWriter javaScriptWriter) {
        super.setWriter(javaScriptWriter);
        this.validatorWriterManager.setWriter(javaScriptWriter);
    }
}
