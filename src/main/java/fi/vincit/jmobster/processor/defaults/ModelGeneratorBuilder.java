package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.processor.*;

/**
 * Builder for ModelGenerator. Model processor and model field factory are
 * mandatory. If no model naming strategy is given, default is used.
 */
public class ModelGeneratorBuilder {
    private ModelProcessor modelProcessor;
    private ModelFieldFactory modelFieldFactory;
    private ModelNamingStrategy modelNamingStrategy;

    public ModelGeneratorBuilder setModelProcessor( ModelProcessor modelProcessor ) {
        this.modelProcessor = modelProcessor;
        return this;
    }

    public ModelGeneratorBuilder setModelFieldFactory( ModelFieldFactory modelFieldFactory ) {
        this.modelFieldFactory = modelFieldFactory;
        return this;
    }

    public ModelGeneratorBuilder setModelFieldFactory(FieldScanMode scanMode,
                                                      FieldValueConverter valueConverter,
                                                      ValidatorScanner validatorScanner ) {
        this.modelFieldFactory = new DefaultModelFieldFactory(scanMode, valueConverter, validatorScanner);
        return this;
    }

    public ModelGeneratorBuilder setModelNamingStrategy( ModelNamingStrategy modelNamingStrategy ) {
        this.modelNamingStrategy = modelNamingStrategy;
        return this;
    }

    public DefaultModelGenerator createDefaultModelGenerator() {
        checkAndCreateDefaults();
        return new DefaultModelGenerator( modelProcessor, modelFieldFactory, modelNamingStrategy );
    }

    private void checkAndCreateDefaults() {
        if( modelProcessor == null ) {
            throw new IllegalArgumentException("No model processor defined");
        }
        if( modelFieldFactory == null ) {
            throw new IllegalArgumentException("No model field factory defined");
        }
        if( modelNamingStrategy == null ) {
            modelNamingStrategy = new DefaultNamingStrategy();
        }
    }
}