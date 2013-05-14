package fi.vincit.jmobster.util.combination;

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
import org.junit.Test;

import java.lang.annotation.Annotation;

import static fi.vincit.jmobster.util.test.TestUtil.collectionFromObjects;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class CombinationManagerTest {

    public static class ConcreteTestAnnotation extends FieldAnnotation {
        public ConcreteTestAnnotation() {
            super( mock(Annotation.class) );
        }

        @Override
        public Class getType() { return getClass(); }
    }
    
    public static class Type1 extends ConcreteTestAnnotation {}
    public static class Type2 extends ConcreteTestAnnotation {}
    public static class Type3 extends ConcreteTestAnnotation {}

    @Test
    public void testNoRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get());
        assertFalse( cm.matches( collectionFromObjects( new Type1() ) ) );
        assertFalse( cm.containsClass( Type1.class ) );
        assertEquals( null, cm.findClass( Type1.class ) );
    }

    @Test
    public void testOneRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get( Type1.class ));
        assertTrue(cm.matches( collectionFromObjects( new Type1() ) ) );
        assertTrue( cm.containsClass( Type1.class ) );
        assertEquals( Type1.class, cm.findClass( Type1.class ) );
    }

    @Test
    public void testTwoRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(Type1.class, Type2.class));

        assertTrue(cm.matches( collectionFromObjects( new Type1(), new Type2() ) ) );
        assertTrue( cm.containsClass( Type1.class ) );
        assertEquals( Type1.class, cm.findClass( Type1.class ) );
        assertEquals( Type2.class, cm.findClass( Type2.class ) );
    }

    @Test
    public void testTwoRequiredOneOptional() {
        CombinationManager cm = new CombinationManager(
                RequiredTypes.get(Type1.class, Type2.class),
                OptionalTypes.get( Type3.class )
        );
        assertFalse( cm.matches( collectionFromObjects( new Type1(), new Type3() ) ) );

        assertTrue( cm.matches( collectionFromObjects( new Type1(), new Type2()) ) );
        assertTrue( cm.matches( collectionFromObjects( new Type1(), new Type2(), new Type3()) ) );
        assertTrue( cm.containsClass( Type1.class ) );
        assertTrue( cm.containsClass( Type3.class ) );
        assertEquals( Type1.class, cm.findClass( Type1.class ) );
        assertEquals( Type2.class, cm.findClass( Type2.class ) );
        assertEquals( Type3.class, cm.findClass( Type3.class ) );
    }

    @Test
    public void testNoClassFound() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(Type1.class), OptionalTypes.get(Type3.class));
        assertNull( cm.findClass( Type2.class ) );
        assertFalse( cm.containsClass( Type2.class ) );
    }

    @Test
    public void testEmptyCollections() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(), OptionalTypes.get() );
        assertFalse( cm.containsClass( Type2.class ) );
        assertNull( cm.findClass( Type2.class ) );
    }
}
