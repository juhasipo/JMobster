package fi.vincit.jmobster.processor.defaults.validator;

import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.util.AbstractAnnotation;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnnotationBagTest {
    public static class Type1 extends AbstractAnnotation {}
    public static class Type2 extends AbstractAnnotation {};

    @Test
    public void testFindAnnotation() {
        AnnotationBag bag = new AnnotationBag();
        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        when(annotation.getAnnotation()).thenReturn(new Type1());

        bag.addAnnotation(annotation);

        Type1 foundAnnotation = bag.getAnnotation(Type1.class);
        assertTrue(bag.hasAnnotation(Type1.class));
        assertEquals(foundAnnotation.getClass(), Type1.class);
    }

    @Test
    public void testDontFindAnnotation() {
        AnnotationBag bag = new AnnotationBag();
        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        when(annotation.getAnnotation()).thenReturn(new Type1());

        bag.addAnnotation(annotation);

        Type2 foundAnnotation = bag.getAnnotation(Type2.class);
        assertFalse( bag.hasAnnotation( Type2.class ) );
        assertNull( foundAnnotation );
    }
}
