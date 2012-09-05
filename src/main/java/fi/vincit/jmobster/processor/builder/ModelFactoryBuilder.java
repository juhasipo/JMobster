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

import fi.vincit.jmobster.processor.ModelFieldFactory;
import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.defaults.DefaultModelFactory;
import fi.vincit.jmobster.processor.defaults.DefaultNamingStrategy;

public class ModelFactoryBuilder {
    private ModelFieldFactory modelFieldFactory;
    private ModelNamingStrategy modelNamingStrategy;

    public ModelFactoryBuilder setModelFieldFactory( ModelFieldFactory modelFieldFactory ) {
        this.modelFieldFactory = modelFieldFactory;
        return this;
    }

    public ModelFactoryBuilder setModelNamingStrategy( ModelNamingStrategy modelNamingStrategy ) {
        this.modelNamingStrategy = modelNamingStrategy;
        return this;
    }

    public DefaultModelFactory createDefaultModelFactory() {
        if( modelFieldFactory == null ) {
            throw new IllegalArgumentException("No model field factory defined");
        }
        if( modelNamingStrategy == null ) {
            modelNamingStrategy = new DefaultNamingStrategy();
        }
        return new DefaultModelFactory( modelFieldFactory, modelNamingStrategy );
    }
}