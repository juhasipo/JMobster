package fi.vincit.jmobster.processor.model;

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

import fi.vincit.jmobster.util.test.TestUtil;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTest {
    @Test
    public void testConstructModelNoFields() throws Exception {
        class TestModelClass {}
        Collection<ModelField> modelFields = Collections.emptyList();

        Model model = new Model(TestModelClass.class, "TestModel", modelFields);

        assertEquals("TestModel", model.getName());
        assertEquals( TestModelClass.class, model.getModelClass() );
        assertTrue(model.getFields().isEmpty());
        assertFalse(model.hasValidations());
    }

    @Test
    public void testConstructModelValidations() throws Exception {
        class TestModelClass {}
        ModelField fieldWithoutValidation = mock(ModelField.class);
        when(fieldWithoutValidation.hasAnnotations()).thenReturn(false);
        ModelField fieldWithValidation = mock(ModelField.class);
        when(fieldWithoutValidation.hasAnnotations()).thenReturn(true);

        Model model = new Model(TestModelClass.class, "TestModel", TestUtil.collectionFromObjects(fieldWithoutValidation, fieldWithValidation));

        assertEquals("TestModel", model.getName());
        assertEquals( TestModelClass.class, model.getModelClass() );
        assertEquals(2, model.getFields().size());
        assertTrue(model.hasValidations());
    }

    @Test
    public void testConstructModelNoValidations() throws Exception {
        class TestModelClass {}
        ModelField fieldWithoutValidation1 = mock(ModelField.class);
        when(fieldWithoutValidation1.hasAnnotations()).thenReturn( false );
        ModelField fieldWithoutValidation2 = mock(ModelField.class);
        when(fieldWithoutValidation1.hasAnnotations()).thenReturn( false );

        Model model = new Model(TestModelClass.class, "TestModel", TestUtil.collectionFromObjects(fieldWithoutValidation1, fieldWithoutValidation2));

        assertEquals("TestModel", model.getName());
        assertEquals( TestModelClass.class, model.getModelClass() );
        assertEquals(2, model.getFields().size());
        assertFalse(model.hasValidations());
    }
}
