package fi.vincit.jmobster.processor;

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

import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.groups.GroupMode;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * ValidatorScanner scans fields and properties for validator
 * annotations and constructs equivalent validators. The scanner
 * supports validator filtering by groups but actual implementation
 * is not required by the subclasses.
 */
public interface ValidatorScanner {
    /**
     * Gets the validators for the given field
     * @param field Field to scan
     * @return Validation annotation. If nothing found returns an empty collection.
     */
    Collection<Validator> getValidators( Field field );

    /**
     * Gets the validators for the given property
     * @param property Property to scan
     * @return Validation annotation. If nothing found returns an empty collection.
     */
    Collection<Validator> getValidators( PropertyDescriptor property );

    /**
     * Sets filter group mode and filter groups for validation scanning.
     * @param groupMode Filter group mode
     * @param groups Groups to filter with
     */
    void setFilterGroups( GroupMode groupMode, Collection<Class> groups );
}
