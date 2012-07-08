package fi.vincit.jmobster.processor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombinationManagerTest {

    @Test
    public void testOneRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(String.class));
        //assertTrue(cm.test(String.class));
        assertTrue(cm.containsClass(String.class));
        assertEquals(String.class, cm.findClass(String.class));
    }

    @Test
    public void testTwoRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(String.class, Integer.class));
        //assertFalse( cm.test( String.class ) );

        //assertTrue( cm.test( String.class, Integer.class ) );
        assertTrue(cm.containsClass(String.class));
        assertEquals(String.class, cm.findClass(String.class));
        assertEquals(Integer.class, cm.findClass(Integer.class));
    }

    @Test
    public void testTwoRequiredOneOptional() {
        CombinationManager cm = new CombinationManager(
                RequiredTypes.get(String.class, Integer.class),
                OptionalTypes.get(Float.class)
        );
        //assertFalse( cm.supports( toList(String.class, Float.class) ) );

        //assertTrue( cm.supports( String.class, Integer.class ) );
        //assertTrue( cm.supports( String.class, Integer.class, Float.class ) );
        assertTrue(cm.containsClass(String.class));
        assertTrue(cm.containsClass(Float.class));
        assertEquals(String.class, cm.findClass(String.class));
        assertEquals(Integer.class, cm.findClass(Integer.class));
        assertEquals(Float.class, cm.findClass(Float.class));
    }

    private static List<Class> toList(Class...classes) {
        List<Class> list = new ArrayList<Class>(classes.length);
        for( Class c : classes ) {
            list.add(c);
        }
        return list;
    }
}
