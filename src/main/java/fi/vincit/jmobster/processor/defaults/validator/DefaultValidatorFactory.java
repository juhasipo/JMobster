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
import fi.vincit.jmobster.util.combination.OptionalTypes;
import fi.vincit.jmobster.util.combination.RequiredTypes;

import javax.validation.constraints.*;

/**
 * Default implementation of validator factory. Contains
 * validator constructors for basic JSR-303 validation annotations.
 * Use {@link BaseValidatorFactory#setValidator(ValidatorConstructor)} to
 * add more validators.
 */
public class DefaultValidatorFactory extends BaseValidatorFactory {
    public DefaultValidatorFactory() {
        setValidator(
                SizeValidator.class,
                RequiredTypes.get(Size.class),
                OptionalTypes.get());
        setValidator(
                PatternValidator.class,
                RequiredTypes.get(Pattern.class),
                OptionalTypes.get(OverridePattern.class));
        setValidator(
                MinValidator.class,
                RequiredTypes.get(Min.class),
                OptionalTypes.get());
        setValidator(
                MaxValidator.class,
                RequiredTypes.get(Max.class),
                OptionalTypes.get());
        setValidator(
                NotNullValidator.class,
                RequiredTypes.get(NotNull.class),
                OptionalTypes.get());
    }
}
