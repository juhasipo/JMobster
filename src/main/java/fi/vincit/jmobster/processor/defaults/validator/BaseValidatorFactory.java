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

import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.processor.ValidatorFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseValidatorFactory implements ValidatorFactory {

    protected static interface ValidatorCreator {
        Validator create(Annotation annotation);
    }

    final private Map<Class, ValidatorCreator> validators;

    protected BaseValidatorFactory() {
        validators = new HashMap<Class, ValidatorCreator>();
    }

    protected void setValidator(Class type, ValidatorCreator validatorCreator) {
        validators.put(type, validatorCreator);
    }

    @Override
    public Validator getValidator( Annotation annotation ) {
        if( isValidationAnnotation(annotation) ) {
            return validators.get(annotation.annotationType()).create(annotation);
        } else {
            return null;
        }
    }

    @Override
    public boolean isValidationAnnotation( Annotation annotation ) {
        Class type = annotation.annotationType();
        return validators.containsKey( type );
    }
}
