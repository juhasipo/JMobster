package fi.vincit.jmobster.processor;

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

import fi.vincit.jmobster.processor.model.Model;

import java.util.Collection;

public interface ModelFactory {
    /**
     * Creates a model representing the given class
     * @param clazz Class
     * @return Model
     */
    Model create(Class clazz);

    /**
     * Creates models for given collection of classes
     * @param classes Classes
     * @return Models
     */
    Collection<Model> createAll(Collection<Class> classes);

    /**
     * Creates models for given classes
     * @param classes One or more classes
     * @return Models
     */
    Collection<Model> createAll( Class... classes );

    /**
     * Sets validator filter groups
     * @param groupMode Group mode
     * @param classes Classes used for filtering
     */
    void setValidatorFilterGroups( GroupMode groupMode, Class... classes );
}
