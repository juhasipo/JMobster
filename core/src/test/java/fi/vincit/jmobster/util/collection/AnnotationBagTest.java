package fi.vincit.jmobster.util.collection;

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
import fi.vincit.jmobster.util.AbstractAnnotation;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnnotationBagTest {
    public static class Type1 extends AbstractAnnotation {}
    public static class Type2 extends AbstractAnnotation {}

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

    @Test
    public void testHasAnnotations() {
        AnnotationBag bag = new AnnotationBag();
        FieldAnnotation annotation = mock(FieldAnnotation.class);
        when(annotation.getType()).thenReturn(Type1.class);
        when(annotation.getAnnotation()).thenReturn(new Type1());

        bag.addAnnotation(annotation);

        assertTrue(bag.hasAnnotations());
    }

    @Test
    public void testDoesHaveAnnotations() {
        AnnotationBag bag = new AnnotationBag();

        assertFalse(bag.hasAnnotations());
    }
}
