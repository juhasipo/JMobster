package fi.vincit.jmobster.processor.frameworks.backbone;

/*
 * Copyright 2012-2013 Juha Siponen
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
import fi.vincit.jmobster.processor.ValidatorWriterManager;
import fi.vincit.jmobster.processor.defaults.DummyDataWriter;
import fi.vincit.jmobster.processor.languages.javascript.BaseJavaScriptModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.processor.languages.javascript.writer.OutputMode;
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
public class ValidatorProcessor extends BaseJavaScriptModelProcessor {

    private ValidatorWriterManager<JavaScriptContext, JavaScriptWriter> validatorWriterManager;
    private ItemHandler<Validator> validatorWriter;

    public static class Builder {
        private String name = "validation";
        private FieldValueConverter valueConverter;
        private JavaScriptContext context = new JavaScriptContext(
                new JavaScriptWriter(DummyDataWriter.getInstance()),
                OutputMode.JAVASCRIPT
        );
        private ValidatorWriterManager<JavaScriptContext, JavaScriptWriter> validatorWriterManager;

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

        public Builder setWriter(DataWriter writer, OutputMode outputMode) {
            this.context = new JavaScriptContext(writer, outputMode);
            return this;
        }

        public Builder setValidatorWriters(ValidatorWriterManager<JavaScriptContext, JavaScriptWriter> validatorWriterManager) {
            this.validatorWriterManager = validatorWriterManager;
            return this;
        }

        public ValidatorProcessor build() {
            return new ValidatorProcessor(this);
        }
    }

    private ValidatorProcessor(Builder builder) {
        super(builder.name);

        this.validatorWriterManager = builder.validatorWriterManager;
        setLanguageContext(builder.context);
        setFieldValueConverter(builder.valueConverter);

        this.validatorWriter = new ItemHandler<Validator>() {
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
    public void processModel(Model model, ItemStatus status) {
        ItemProcessor.process( model.getFields() ).with(new ItemHandler<ModelField>() {
            @Override
            public void process( ModelField field, ItemStatus status ) {
                getWriter().writeKey( field.getName() ).startBlock();
                ItemProcessor.process(field.getValidators()).with(validatorWriter);
                getWriter().endBlock( status );
            }
        });
    }

    @Override
    public void endProcessing(ItemStatus status) throws IOException {
        getWriter().endBlock( status );
    }

    @Override
    public void setLanguageContext(JavaScriptContext context) {
        super.setLanguageContext(context);
        this.validatorWriterManager.setLanguageContext(context);
    }
}
