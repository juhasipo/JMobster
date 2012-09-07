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

import fi.vincit.jmobster.processor.ValidatorFactory;
import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Base class for validator factories.
 */
public abstract class BaseValidatorFactory implements ValidatorFactory {

    private static final Logger LOG = LoggerFactory.getLogger( BaseValidatorFactory.class );

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
     * Adds new validator constructor to the factory. The order in which the constructors
     * are added determines the priority. Constructors are always executed in same order:
     * First added first executed.
     * @param validatorClass Validator class
     * @param requiredTypes Required types for validator
     * @param optionalTypes Optional types for validator
     */
    @Override
    public void setValidator(Class validatorClass, RequiredTypes requiredTypes, OptionalTypes optionalTypes) {
        validatorConstructors.add( new ValidatorConstructor(validatorClass, requiredTypes, optionalTypes) );
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
