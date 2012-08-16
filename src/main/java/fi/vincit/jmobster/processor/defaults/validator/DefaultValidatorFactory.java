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

import fi.vincit.jmobster.processor.defaults.validator.BaseValidatorFactory;
import fi.vincit.jmobster.processor.defaults.validator.SizeValidator;
import fi.vincit.jmobster.processor.model.Validator;

import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;

public class DefaultValidatorFactory extends BaseValidatorFactory {
    public DefaultValidatorFactory() {
        setValidator(Size.class, new ValidatorCreator() {
            @Override
            public Validator create( Annotation annotation ) {
                return new SizeValidator((Size)annotation);
            }
        } );
    }
}