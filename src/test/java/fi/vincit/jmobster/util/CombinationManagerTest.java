package fi.vincit.jmobster.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static fi.vincit.jmobster.util.TestUtil.listFromObjects;
import static org.junit.Assert.*;

public class CombinationManagerTest {

    @Test
    public void testOneRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get( String.class ));
        //assertTrue(cm.matches( listFromObjects( String.class ) ) );
        assertTrue( cm.containsClass( String.class ) );
        assertEquals(String.class, cm.findClass(String.class));
    }

    @Test
    public void testTwoRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(String.class, Integer.class));
        //assertFalse( cm.test( String.class ) );

        //assertTrue( cm.test( String.class, Integer.class ) );
        assertTrue( cm.containsClass( String.class ) );
        assertEquals(String.class, cm.findClass(String.class));
        assertEquals(Integer.class, cm.findClass(Integer.class));
    }

    @Test
    public void testTwoRequiredOneOptional() {
        CombinationManager cm = new CombinationManager(
                RequiredTypes.get(String.class, Integer.class),
                OptionalTypes.get( Float.class )
        );
        //assertFalse( cm.matches( toList(String.class, Float.class) ) );

        //assertTrue( cm.matches( String.class, Integer.class ) );
        //assertTrue( cm.matches( String.class, Integer.class, Float.class ) );
        assertTrue( cm.containsClass( String.class ) );
        assertTrue( cm.containsClass( Float.class ) );
        assertEquals(String.class, cm.findClass(String.class));
        assertEquals(Integer.class, cm.findClass(Integer.class));
        assertEquals(Float.class, cm.findClass(Float.class));
    }

    @Test
    public void testNoClassFound() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(String.class), OptionalTypes.get(Float.class));
        assertNull(cm.findClass( Integer.class ));
        assertFalse(cm.containsClass(Integer.class));
    }

    @Test
    public void testEmptyCollections() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(), OptionalTypes.get() );
        assertFalse(cm.containsClass(Integer.class));
        assertNull(cm.findClass(Integer.class));
    }
}
