package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.ValidatorConstructor;
import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.processor.model.Validator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of validator factory. Additional
 * validators can be added using {@link fi.vincit.jmobster.processor.ValidatorFactory#setValidator(Class, fi.vincit.jmobster.processor.ValidatorConstructor)}
 * method.
 */
public abstract class BaseValidatorFactory implements ValidatorFactory {

    final private Map<Class, ValidatorConstructor> validatorConstructors;

    protected BaseValidatorFactory() {
        validatorConstructors = new HashMap<Class, ValidatorConstructor>();
    }

    @Override
    public void setValidator(Class type, ValidatorConstructor validatorConstructor) {
        validatorConstructors.put( type, validatorConstructor );
    }

    @Override
    public Validator getValidator( Annotation annotation ) {
        if( isValidationAnnotation(annotation) ) {
            return validatorConstructors.get(annotation.annotationType()).construct( annotation );
        } else {
            return null;
        }
    }

    @Override
    public boolean isValidationAnnotation( Annotation annotation ) {
        Class type = annotation.annotationType();
        return validatorConstructors.containsKey( type );
    }
}
