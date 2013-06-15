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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelFieldTest {

    @Test
    public void testModelFromFieldNoValidators() throws Exception {
        class TestClass { public String testFieldName; }

        Field field = TestUtil.getFieldFromClass(TestClass.class, 0);
        final Class fieldType = String.class;
        Collection<FieldAnnotation> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        assertEquals("testFieldName", modelField.getName());
        assertEquals(fieldType, modelField.getFieldType());
        assertFalse(modelField.hasAnnotations());
        assertFalse(modelField.hasAnnotations());
    }

    @Test
    public void testModelFromPropertyNoValidators() throws Exception {
        class TestClass { private String testFieldName; public String getTestFieldName() { return  testFieldName; } }

        PropertyDescriptor field = TestUtil.getPropertyFromClass( TestClass.class, "testFieldName" );
        final Class fieldType = String.class;
        Collection<FieldAnnotation> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        assertEquals("testFieldName", modelField.getName());
        assertEquals(fieldType, modelField.getFieldType());
        assertFalse(modelField.hasAnnotations());
        assertFalse(modelField.hasAnnotations());
    }

    @Test
    public void testAddValidators() throws Exception {
        class TestClass { public String testFieldName; }

        Field field = TestUtil.getFieldFromClass(TestClass.class, 0);
        Collection<FieldAnnotation> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        FieldAnnotation validator1 = mock(FieldAnnotation.class);
        FieldAnnotation validator2 = mock(FieldAnnotation.class);
        Collection<FieldAnnotation> validatorsToAdd = TestUtil.collectionFromObjects(validator1, validator2);

        modelField.addAnnotations(validatorsToAdd);

        assertTrue(modelField.hasAnnotations());
        assertEquals(2, modelField.getAnnotations().size());
    }

    @Test
    public void testAddAnnotation() throws Exception {
        class TestClass { public String testFieldName; }

        Field field = TestUtil.getFieldFromClass(TestClass.class, 0);
        Collection<FieldAnnotation> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        FieldAnnotation annotation1 = mock(FieldAnnotation.class);
        when(annotation1.getType()).thenReturn(TestClass.class);
        when(annotation1.getAnnotation()).thenReturn(mock(Annotation.class));
        FieldAnnotation annotation2 = mock(FieldAnnotation.class);

        modelField.addAnnotation(annotation1);
        modelField.addAnnotation(annotation2);

        assertTrue( modelField.hasAnnotations() );
    }
}
