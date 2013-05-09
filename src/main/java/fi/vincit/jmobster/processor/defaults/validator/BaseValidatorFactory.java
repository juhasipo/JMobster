package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Base class for validator factories. Extends this class to create factories for a
 * custom set of validators. If you only need to add more validators to an existing factory
 * you don't need to create new class. Use {@link BaseValidatorFactory#setValidator(ValidatorConstructor)} or
 * {@link BaseValidatorFactory#setValidator(Class, fi.vincit.jmobster.util.combination.RequiredTypes, fi.vincit.jmobster.util.combination.OptionalTypes)}
 * to add new validators to the factory.
 */
public abstract class BaseValidatorFactory implements ValidatorFactory {

    // Use list so that the order is always the same when returning
    // validators. This helps testing but is a good feature for some cases
    final private List<ValidatorConstructor> validatorConstructors;

    protected BaseValidatorFactory() {
        validatorConstructors = new ArrayList<ValidatorConstructor>();
    }

    /**
     * Adds new validator constructor to the factory. The order in which the constructors
     * are added determines the priority. Constructors are always executed in same order:
     * First added first executed.
     * @param validatorConstructor Constructor that will create the validator
     */
    @Override
    public void setValidator(ValidatorConstructor validatorConstructor) {
        validatorConstructors.add( validatorConstructor );
    }

    /**
     * Adds new validators to the factory. The order in which the validators
     * are added determines the priority. Validators are always executed in same order:
     * First added first executed.
     * @param validatorClasses Validator classes to set
     */
    @Override
    public void setValidator(Class... validatorClasses) {
        for( Class validatorClass : validatorClasses ) {
            validatorConstructors.add( new ValidatorConstructor(validatorClass) );
        }
    }

    /**
     * Creates all validators that can be constructed from the given field annotations.
     * The order is determined by the order of adding validator constructors.
     * @param annotations Collection of field annotations for which validators should be created.
     * @return Created validators
     */
    @Override
    public List<Validator> createValidators( Collection<FieldAnnotation> annotations ) {
        // Use array list so that the order is always the same when returning
        // validators. This helps testing but is a good feature for some cases
        List<Validator> validators = new ArrayList<Validator>(annotations.size());

        for( ValidatorConstructor validatorConstructor : validatorConstructors ) {
            Validator validator = validatorConstructor.construct(annotations);
            if( validator != null ) {
                validators.add( validator );
            }
        }
        return validators;
    }
}
