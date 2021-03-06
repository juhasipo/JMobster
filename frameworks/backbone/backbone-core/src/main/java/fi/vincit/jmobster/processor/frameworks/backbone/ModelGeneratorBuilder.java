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

import fi.vincit.jmobster.exception.BuildingError;
import fi.vincit.jmobster.processor.FieldValueConverter;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.defaults.DefaultModelGenerator;
import fi.vincit.jmobster.processor.defaults.validator.ValidatorWriterSet;
import fi.vincit.jmobster.processor.frameworks.backbone.type.BackboneFieldTypeConverterManager;
import fi.vincit.jmobster.processor.frameworks.base.BaseFieldTypeConverterManager;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptModelGenerator;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.EnumConverter;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.util.ConverterMode;

/**
 * ModelGenerator builder for Backbone-validation. Model processor and model field factory are
 * mandatory. If no model naming strategy is given, default is used.
 */
public class ModelGeneratorBuilder {
    private ModelProcessor<JavaScriptContext, JavaScriptWriter> modelProcessor;
    private FieldValueConverter fieldValueConverter;
    private BaseFieldTypeConverterManager fieldTypeConverterManager;
    private JavaScriptContext context;
    private ValidatorWriterSet<JavaScriptContext, JavaScriptWriter> validatorWriterManager;

    public ModelGeneratorBuilder setLanguageContext(JavaScriptContext context) {
        this.context = context;
        return this;
    }

    public ModelGeneratorBuilder setModelProcessor(ModelProcessor modelProcessor) {
        this.modelProcessor = modelProcessor;
        return this;
    }

    public ModelGeneratorBuilder setFieldValueConverter(FieldValueConverter fieldValueConverter) {
        this.fieldValueConverter = fieldValueConverter;
        return this;
    }

    public ModelGeneratorBuilder setFieldTypeConverterManager(BaseFieldTypeConverterManager fieldTypeConverterManager) {
        this.fieldTypeConverterManager = fieldTypeConverterManager;
        return this;
    }

    public ModelGeneratorBuilder setValidatorWriterManager(ValidatorWriterSet<JavaScriptContext, JavaScriptWriter> validatorWriterManager) {
        this.validatorWriterManager = validatorWriterManager;
        return this;
    }

    public DefaultModelGenerator build() {
        if( this.modelProcessor == null ) {
            throwIfNull(context, "LanguageContext");

            if( this.fieldTypeConverterManager == null ) {
                this.fieldTypeConverterManager = new BackboneFieldTypeConverterManager();
            }

            if( fieldValueConverter == null ) {
                this.fieldValueConverter = new JavaToJSValueConverter(
                        ConverterMode.NULL_AS_DEFAULT,
                        EnumConverter.EnumMode.STRING,
                        JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN
                );
            }
            modelProcessor = new fi.vincit.jmobster.processor.frameworks.backbone.BackboneModelProcessor
                    .Builder(context)
                    .useDefaultModelProcessors(validatorWriterManager)
                    .build();
        }
        return new JavaScriptModelGenerator( modelProcessor );
    }

    private void throwIfNull(Object object, String name) {
        if( object == null ) {
            throw new BuildingError(name + " must be set");
        }
    }
}