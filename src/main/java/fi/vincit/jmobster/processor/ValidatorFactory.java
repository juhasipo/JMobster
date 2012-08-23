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

import fi.vincit.jmobster.processor.model.Validator;

import java.lang.annotation.Annotation;

/**
 * Creates new validator instances from annotation instances.
 */
public interface ValidatorFactory {

    /**
     * Returns a new validator instance for the given annotation.
     * To prevent null pointer exceptions, check if the validator
     * can be constructed with {@link ValidatorFactory#isValidationAnnotation(java.lang.annotation.Annotation)}
     * method.
     * @param annotation Annotation instance
     * @return New validator instance if exists, otherwise null.
     */
    Validator getValidator(Annotation annotation);

    /**
     *
     * @param annotation Annotation
     * @return True if the given annotation has a validator set
     */
    boolean isValidationAnnotation(Annotation annotation);

    /**
     * Configures validator constructor for the given type
     * @param type Type (Annotation class)
     * @param validatorConstructor Constructor that will create the validator
     */
    void setValidator(Class type, ValidatorConstructor validatorConstructor);
}
