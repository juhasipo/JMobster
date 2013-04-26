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

import fi.vincit.jmobster.processor.defaults.validator.ValidatorConstructor;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;

import java.util.Collection;
import java.util.List;

/**
 * Creates new validator instances from annotation instances.
 */
public interface ValidatorFactory {

    /**
     * Returns a new validator instance for the given set of annotations.
     * All possible combinations of validators are created.
     * @param annotations List of all annotations
     * @return List of created validators. Empty list if no validators are created.
     */
    List<Validator> createValidators(Collection<FieldAnnotation> annotations);

    /**
     * Configures validator constructor for the given type
     * @param validatorConstructor Constructor that will create the validator
     */
    void setValidator(ValidatorConstructor validatorConstructor);

    /**
     * Adds new validators to the factory
     * @param validatorClasses Validator classes to set
     */
    void setValidator(Class... validatorClasses);
}
