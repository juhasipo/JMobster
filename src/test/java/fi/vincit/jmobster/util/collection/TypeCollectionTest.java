package fi.vincit.jmobster.util.collection;

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
