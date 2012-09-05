package fi.vincit.jmobster.processor.builder;

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
import fi.vincit.jmobster.processor.defaults.DefaultModelGenerator;

/**
 * Builder for ModelGenerator. Model processor and model field factory are
 * mandatory. If no model naming strategy is given, default is used.
 */
public class ModelGeneratorBuilder {
    private ModelProcessor modelProcessor;

    public ModelGeneratorBuilder setModelProcessor( ModelProcessor modelProcessor ) {
        this.modelProcessor = modelProcessor;
        return this;
    }

    public DefaultModelGenerator createDefaultModelGenerator() {
        if( modelProcessor == null ) {
            throw new IllegalArgumentException("No model processor defined");
        }
        return new DefaultModelGenerator( modelProcessor );
    }
}