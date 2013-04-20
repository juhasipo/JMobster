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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Combines one or more {@ValidatorFactory} instances into single validator factory. The
 * order in which the factories are given tells which of the factories is in
 * higher priority. If the first encountered factory can create validators, the
 * rest of the validators will not be executed.
 *
 * The validator the {@link CombinedValidatorFactory} has {@link ValidatorConstructor}'s
 * of its own, they are in the highest priority.
 */
public class CombinedValidatorFactory extends BaseValidatorFactory {

    private List<ValidatorFactory> validatorFactories = new ArrayList<ValidatorFactory>();

    /**
     * Constructs a combined validator factory. It requires at least one
     * {@link ValidatorFactory}.
     * @param validatorFactory Required validator factory
     * @param additionalFactories Additional validator factories
     */
    public CombinedValidatorFactory(ValidatorFactory validatorFactory, ValidatorFactory... additionalFactories) {
        validatorFactories.add(validatorFactory);
        for( ValidatorFactory additionalFactory : additionalFactories ) {
            validatorFactories.add(additionalFactory);
        }
    }

    @Override
    public List<Validator> createValidators(Collection<FieldAnnotation> annotations) {
        List<Validator> validators;
        validators = super.createValidators(annotations);
        if( !validators.isEmpty() ) {
            return validators;
        }
        for( ValidatorFactory factory : validatorFactories ) {
            validators = factory.createValidators(annotations);
            if( !validators.isEmpty() ) {
                return validators;
            }
        }

        return validators;
    }


    @Override
    public void setValidator(ValidatorConstructor validatorConstructor) {
        super.setValidator(validatorConstructor);
    }

    @Override
    public void setValidator(Class validatorClass, RequiredTypes requiredTypes, OptionalTypes optionalTypes) {
        super.setValidator(validatorClass, requiredTypes, optionalTypes);
    }
}
