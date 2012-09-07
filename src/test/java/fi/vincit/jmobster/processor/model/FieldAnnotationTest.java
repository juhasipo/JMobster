package fi.vincit.jmobster.processor.model;

import org.junit.Test;

import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FieldAnnotationTest {
    @Test
    public void testConvertToFieldAnnotations() throws Exception {
        Annotation annotation = mock(Annotation.class);

        //List<FieldAnnotation> annotationList = FieldAnnotation.convertToFieldAnnotations();
    }

    @Test(expected = NullPointerException.class)
    public void testNullAnnotation() {
        new FieldAnnotation(null);
    }

    @Test
    public void testGetGroups() throws Exception {
        class Group1 {}
        class Group2 {}
        Size annotation = mock(Size.class);
        when(annotation.groups()).thenReturn(new Class[] { Group1.class, Group2.class });

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertEquals(2, fieldAnnotation.getGroups().length);
        assertEquals(Group1.class, fieldAnnotation.getGroups()[0]);
        assertEquals(Group2.class, fieldAnnotation.getGroups()[1]);
    }

    @Test
    public void testHasGroups() throws Exception {
        class Group1 {}
        class Group2 {}
        Size annotation = mock(Size.class);
        when(annotation.groups()).thenReturn(new Class[] { Group1.class, Group2.class });

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertTrue( fieldAnnotation.hasGroups() );
    }

    @Test
    public void testNoGroups() throws Exception {
        Size annotation = mock(Size.class);
        when(annotation.groups()).thenReturn(new Class[] {});

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertEquals(0, fieldAnnotation.getGroups().length);
    }

    @Test
    public void testHasNoGroups() throws Exception {
        Size annotation = mock(Size.class);
        when(annotation.groups()).thenReturn(new Class[] {});

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertFalse( fieldAnnotation.hasGroups() );
    }

    @Test
    public void testNoGroupsWhenNullGiven() throws Exception {
        Size annotation = mock(Size.class);
        when(annotation.groups()).thenReturn(null);

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertEquals( 0, fieldAnnotation.getGroups().length );
    }

    @Test
    public void testHasNoGroupsWhenNullGiven() throws Exception {
        Size annotation = mock(Size.class);
        when(annotation.groups()).thenReturn(null);

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertFalse( fieldAnnotation.hasGroups() );
    }


    @Test
    public void testNoGroupsWhenInvocationTargetException() throws Exception {
        // TODO:
    }

    @Test
    public void testNoGroupsWhenIllegalAccessException() throws Exception {
        // TODO: Throw exception by making groups private
    }



    @Test
    public void testGetType() throws Exception {
        Size annotation = mock(Size.class);
        final Class annotationType = Size.class;
        when(annotation.annotationType()).thenReturn(annotationType);

        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertEquals( Size.class, fieldAnnotation.getType() );
    }

    @Test
    public void testGetAnnotation() throws Exception {
        Size annotation = mock(Size.class);
        FieldAnnotation fieldAnnotation = new FieldAnnotation(annotation);
        assertEquals(annotation, fieldAnnotation.getAnnotation());
    }
}
