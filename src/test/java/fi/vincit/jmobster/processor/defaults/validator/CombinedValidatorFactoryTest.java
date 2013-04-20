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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

public class CombinedValidatorFactoryTest {

    @Mock ValidatorFactory validatorFactoryPrimary;
    @Mock ValidatorFactory validatorFactorySecondary;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSingleFactory() {
        CombinedValidatorFactory validatorFactory = new CombinedValidatorFactory(validatorFactoryPrimary);
        List<FieldAnnotation> annotations = new ArrayList<FieldAnnotation>();
        validatorFactory.createValidators(annotations);

        verify(validatorFactoryPrimary).createValidators(annotations);
    }

    @Test
    public void testTwoFactories() {
        CombinedValidatorFactory validatorFactory =
                new CombinedValidatorFactory(validatorFactoryPrimary, validatorFactorySecondary);
        List<FieldAnnotation> annotations = new ArrayList<FieldAnnotation>();
        validatorFactory.createValidators(annotations);

        InOrder order = inOrder(validatorFactoryPrimary, validatorFactorySecondary);
        order.verify(validatorFactoryPrimary).createValidators(annotations);
        order.verify(validatorFactorySecondary).createValidators(annotations);
    }

    @Test
    public void testTwoFactories_FirstReturns() {
        CombinedValidatorFactory validatorFactory =
                new CombinedValidatorFactory(validatorFactoryPrimary, validatorFactorySecondary);
        when(validatorFactoryPrimary.createValidators(anyList())).thenReturn(
                (List) Arrays.asList(mock(ValidatorConstructor.class))
        );
        List<FieldAnnotation> annotations = new ArrayList<FieldAnnotation>();
        validatorFactory.createValidators(annotations);

        InOrder order = inOrder(validatorFactoryPrimary, validatorFactorySecondary);
        order.verify(validatorFactoryPrimary).createValidators(annotations);
        order.verify(validatorFactorySecondary, never()).createValidators(anyList());
    }

}
