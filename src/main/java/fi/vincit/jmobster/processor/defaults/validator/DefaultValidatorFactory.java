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

import fi.vincit.jmobster.annotation.OverridePattern;
import fi.vincit.jmobster.processor.ValidatorConstructor;
import fi.vincit.jmobster.processor.model.Validator;
import fi.vincit.jmobster.util.CombinationManager;
import fi.vincit.jmobster.util.OptionalTypes;
import fi.vincit.jmobster.util.RequiredTypes;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Default implementation of validator factory. Contains
 * validator constructors for basic JSR-303 validation annotations.
 */
public class DefaultValidatorFactory extends BaseValidatorFactory {
    public DefaultValidatorFactory() {
        setValidator(new ValidatorConstructor(
                SizeValidator.class,
                RequiredTypes.get(Size.class),
                OptionalTypes.get()));
        setValidator(new ValidatorConstructor(
                PatternValidator.class,
                RequiredTypes.get(Pattern.class),
                OptionalTypes.get(OverridePattern.class)));
    }
}
