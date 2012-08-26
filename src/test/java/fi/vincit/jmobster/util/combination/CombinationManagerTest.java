package fi.vincit.jmobster.util.combination;

import org.junit.Test;

public class CombinationManagerTest {

    @Test
    public void testOneRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get( String.class ));
        //assertTrue(cm.matches( listFromObjects( String.class ) ) );
        Assert.assertTrue( cm.containsClass( String.class ) );
        Assert.assertEquals( String.class, cm.findClass( String.class ) );
    }

    @Test
    public void testTwoRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(String.class, Integer.class));
        //assertFalse( cm.test( String.class ) );

        //assertTrue( cm.test( String.class, Integer.class ) );
        Assert.assertTrue( cm.containsClass( String.class ) );
        Assert.assertEquals( String.class, cm.findClass( String.class ) );
        Assert.assertEquals( Integer.class, cm.findClass( Integer.class ) );
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
        Assert.assertTrue( cm.containsClass( String.class ) );
        Assert.assertTrue( cm.containsClass( Float.class ) );
        Assert.assertEquals( String.class, cm.findClass( String.class ) );
        Assert.assertEquals( Integer.class, cm.findClass( Integer.class ) );
        Assert.assertEquals( Float.class, cm.findClass( Float.class ) );
    }

    @Test
    public void testNoClassFound() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(String.class), OptionalTypes.get(Float.class));
        Assert.assertNull( cm.findClass( Integer.class ) );
        Assert.assertFalse( cm.containsClass( Integer.class ) );
    }

    @Test
    public void testEmptyCollections() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(), OptionalTypes.get() );
        Assert.assertFalse( cm.containsClass( Integer.class ) );
        Assert.assertNull( cm.findClass( Integer.class ) );
    }
}
