package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.processor.ValidatorScanner;
import fi.vincit.jmobster.processor.model.FieldAnnotation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Default implementation of validator scanner. Extracts all annotations
 * and returns them as FieldAnnotation objects.
 */
public class DefaultValidatorScanner implements ValidatorScanner {

    public DefaultValidatorScanner() {
    }

    @Override
    public Collection<FieldAnnotation> getValidators( Field field ) {
        return FieldAnnotation.convertToFieldAnnotations(field.getAnnotations());
    }

    @Override
    public Collection<FieldAnnotation> getValidators( PropertyDescriptor property ) {
        return FieldAnnotation.convertToFieldAnnotations(property.getReadMethod().getAnnotations());
    }

}
