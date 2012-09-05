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

import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.ModelFieldFactory;
import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DefaultModelFactory implements ModelFactory {

    private ModelFieldFactory modelFieldFactory;
    private ModelNamingStrategy modelNamingStrategy;

    public DefaultModelFactory( ModelFieldFactory modelFieldFactory,
                                ModelNamingStrategy modelNamingStrategy ) {
        this.modelFieldFactory = modelFieldFactory;
        this.modelNamingStrategy = modelNamingStrategy;
    }

    @Override
    public Model create( Class clazz ) {
        return new Model(clazz, modelNamingStrategy.getName(clazz), modelFieldFactory.getFields(clazz));
    }

    @Override
    public Collection<Model> createAll( Collection<Class> classes ) {
        List<Model> models = new ArrayList<Model>(classes.size());
        for( Class clazz : classes ) {
            models.add(create(clazz));
        }
        return models;
    }

    @Override
    public Collection<Model> createAll( Class... classes ) {
        List<Model> models = new ArrayList<Model>(classes.length);
        for( Class clazz : classes ) {
            models.add(create(clazz));
        }
        return models;
    }

    @Override
    public void setValidatorFilterGroups( GroupMode groupMode, Class... classes ) {
        modelFieldFactory.setValidatorFilterGroups( groupMode, Arrays.asList( classes ) );
    }
}
