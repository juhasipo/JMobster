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
import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.processor.ValidatorScanner;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.groups.GroupFilter;
import fi.vincit.jmobster.util.groups.GroupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Default implementation of validator scanner. Uses {@link ValidatorFactory} and {@link GroupManager}
 * to generate and filter validators.
 */
public class DefaultValidatorScanner implements ValidatorScanner {

    private static final Logger LOG = LoggerFactory.getLogger( DefaultValidatorScanner.class );

    private ValidatorFactory validatorFactory;
    private GroupFilter<FieldAnnotation, Class> filter;

    public DefaultValidatorScanner( ValidatorFactory validatorFactory, GroupManager<Class> groupManager ) {
        this.validatorFactory = validatorFactory;
        this.filter = new GroupFilter<FieldAnnotation, Class>(groupManager);
    }

    @Override
    public Collection<Validator> getValidators( Field field ) {
        return getValidators( convertToFieldAnnotations( field.getAnnotations() ) );
    }

    @Override
    public List<Validator> getValidators( PropertyDescriptor property ) {
        return getValidators( convertToFieldAnnotations( property.getReadMethod().getAnnotations() ) );
    }

    @Override
    public void setFilterGroups( GroupMode groupMode, Collection<Class> groups ) {
        filter.setFilterGroups(groupMode, groups);
    }

    private Collection<FieldAnnotation> convertToFieldAnnotations(Annotation[] annotations) {
        Collection<FieldAnnotation> fieldAnnotations = new ArrayList<FieldAnnotation>(annotations.length);
        for( Annotation annotation : annotations ) {
            FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
            fieldAnnotations.add(fieldAnnotation);
        }
        return fieldAnnotations;
    }

    private List<Validator> getValidators( Collection<FieldAnnotation> annotations ) {
        return validatorFactory.createValidators(filter.filterByGroups(annotations));
    }
}
