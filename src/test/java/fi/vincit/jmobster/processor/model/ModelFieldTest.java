package fi.vincit.jmobster.processor.model;

import fi.vincit.jmobster.util.TestUtil;
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
        Collection<Validator> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        assertEquals("testFieldName", modelField.getName());
        assertEquals(fieldType, modelField.getFieldType());
        assertFalse(modelField.hasValidators());
        assertFalse(modelField.hasAnnotations());
    }

    @Test
    public void testModelFromPropertyNoValidators() throws Exception {
        class TestClass { private String testFieldName; public String getTestFieldName() { return  testFieldName; } }

        PropertyDescriptor field = TestUtil.getPropertyFromClass( TestClass.class, "testFieldName" );
        final Class fieldType = String.class;
        Collection<Validator> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        assertEquals("testFieldName", modelField.getName());
        assertEquals(fieldType, modelField.getFieldType());
        assertFalse(modelField.hasValidators());
        assertFalse(modelField.hasAnnotations());
    }

    @Test
    public void testAddValidators() throws Exception {
        class TestClass { public String testFieldName; }

        Field field = TestUtil.getFieldFromClass(TestClass.class, 0);
        Collection<Validator> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        Validator validator1 = mock(Validator.class);
        Validator validator2 = mock(Validator.class);
        Collection<Validator> validatorsToAdd = TestUtil.collectionFromObjects(validator1, validator2);

        modelField.addValidators(validatorsToAdd);

        assertTrue(modelField.hasValidators());
        assertEquals(2, modelField.getValidators().size());
    }

    @Test
    public void testAddAnnotation() throws Exception {
        class TestClass { public String testFieldName; }

        Field field = TestUtil.getFieldFromClass(TestClass.class, 0);
        Collection<Validator> validators = Collections.emptyList();

        ModelField modelField = new ModelField(field, validators);

        FieldAnnotation annotation1 = mock(FieldAnnotation.class);
        when(annotation1.getType()).thenReturn(TestClass.class);
        when(annotation1.getAnnotation()).thenReturn(mock(Annotation.class));
        FieldAnnotation annotation2 = mock(FieldAnnotation.class);

        modelField.addAnnotation(annotation1);
        modelField.addAnnotation(annotation2);

        assertTrue( modelField.hasAnnotations() );
        final Class classToFind = TestClass.class;
        assertTrue(modelField.hasAnnotation(classToFind));
        assertNotNull(modelField.getAnnotation(classToFind));
    }
}
