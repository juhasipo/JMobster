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

import fi.vincit.jmobster.processor.GroupManager;
import fi.vincit.jmobster.processor.ValidatorScanner;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.processor.ValidatorFactory;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class DefaultValidatorScanner implements ValidatorScanner {

    private ValidatorFactory validatorFactory;
    private GroupManager groupManager;

    public DefaultValidatorScanner( ValidatorFactory validatorFactory, GroupManager groupManager ) {
        this.validatorFactory = validatorFactory;
        this.groupManager = groupManager;
    }

    @Override
    public Collection<Validator> getValidators( Field field, Class... groups ) {
        return getValidators( field.getAnnotations(), groups );
    }

    /**
     * Gets the validation annotations for the given bean property
     * @param property Bean property
     * @param groups Groups to filter validators
     * @return Validation annotations. If nothing found returns an empty list.
     */
    @Override
    public List<Validator> getValidators( PropertyDescriptor property, Class... groups ) {
        return getValidators( property.getReadMethod().getAnnotations(), groups );
    }

    private List<Validator> getValidators( Annotation[] annotations, Class... groups ) {
        List<Validator> validators = new ArrayList<Validator>(annotations.length);
        for( Annotation annotation : annotations ) {
            if( validatorFactory.isValidationAnnotation( annotation ) ) {
                Validator validator = validatorFactory.getValidator(annotation);
                if( groupManager.shouldAddValidator(validator) ) {
                    validators.add( validator );
                }
            }
        }
        return validators;
    }
}
