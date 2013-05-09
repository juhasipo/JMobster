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

import fi.vincit.jmobster.util.collection.TypeCollection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeCollectionTest {

    public static class TestCollection extends TypeCollection<Class> {
        public TestCollection( Class... types ) {
            super( types );
        }
    }

    @Test
    public void testFind() {
        TestCollection tc = new TestCollection(Integer.class, String.class);
        assertEquals(Integer.class, tc.getTypes()[0]);
        assertEquals(String.class, tc.getTypes()[1]);
    }

    @Test
    public void testEmpty() {
        TestCollection tc = new TestCollection();
        assertNotNull(tc.getTypes());
        assertEquals(0, tc.getTypes().length);
    }

    @Test
    public void testNull() {
        Class[] classes = null;
        TestCollection tc = new TestCollection(classes);
        assertNull(tc.getTypes());
    }
}
