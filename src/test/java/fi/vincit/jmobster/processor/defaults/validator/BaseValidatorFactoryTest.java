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

import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.processor.model.Validator;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseValidatorFactoryTest {

    public static class TestValidatorFactory extends BaseValidatorFactory {
    }

    @Test
    public void testCreateValidator() {
        TestValidatorFactory factory = new TestValidatorFactory();
        ValidatorConstructor constructor = mock(ValidatorConstructor.class);
        factory.setValidator(constructor);

        final List<FieldAnnotation> annotationsIn = Collections.emptyList();
        final Validator validatorOut = mock(Validator.class);
        when(constructor.construct(eq(annotationsIn))).thenReturn(validatorOut);

        final List<Validator> validators = factory.createValidators(annotationsIn);
        assertEquals(validators.size(), 1);
    }

    @Test
    public void testCreateValidatorNotFound() {
        TestValidatorFactory factory = new TestValidatorFactory();
        ValidatorConstructor constructor = mock(ValidatorConstructor.class);
        factory.setValidator(constructor);

        final List<FieldAnnotation> annotationsIn = Collections.emptyList();
        when(constructor.construct(eq(annotationsIn))).thenReturn(null);

        final List<Validator> validators = factory.createValidators(annotationsIn);
        assertEquals(validators.size(), 0);
    }

}
