package fi.vincit.jmobster.util.groups;

import fi.vincit.jmobster.processor.GroupMode;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericGroupManagerTest {

    public static interface Group1 {}
    public static interface Group2 {}
    public static interface Group3 {}

    private HasGroups create(Class... groups) {
        HasGroups hasGroups = mock( HasGroups.class );
        final boolean groupsExist = groups.length > 0;
        when( hasGroups.hasGroups() ).thenReturn( groupsExist );
        when( hasGroups.getGroups() ).thenReturn( groups );
        return hasGroups;
    }

    @Test
    public void testAnyOfRequired() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class);
        HasGroups hg2 = create(Group2.class);
        HasGroups hg3 = create(Group1.class, Group2.class);

        assertTrue( groupManager.match( hg1 ) );
        assertTrue( groupManager.match( hg2 ) );
        assertTrue( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredNoGroupsConfigured() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED );
        HasGroups hg1 = create();
        HasGroups hg2 = create(Group1.class);
        HasGroups hg3 = create(Group2.class);
        HasGroups hg4 = create(Group1.class, Group2.class);
        HasGroups hg5 = create(Group1.class, Group3.class);

        assertTrue( groupManager.match( hg1 ) );
        assertTrue( groupManager.match( hg2 ) );
        assertTrue( groupManager.match( hg3 ) );
        assertTrue( groupManager.match( hg4 ) );
        assertTrue( groupManager.match( hg5 ) );
    }

    @Test
    public void testAnyOfRequiredNoGroupsDontAccept() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testAnyOfRequiredNoGroupsAcceptAsDefault() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create();

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAnyOfRequiredWrongGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group3.class);

        assertFalse( groupManager.match( hg1 ) );
    }



    /*
        Exactly tests
     */


    @Test
    public void testExactlyRequired() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class, Group2.class);

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredOneMissing() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredOneWrong() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class, Group3.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredAllWrong() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group3.class, String.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsDontAccept() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsAcceptAsDefault() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create();

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsConfigured() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED );
        HasGroups hg1 = create();

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsConfiguredTryGroups() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED );
        HasGroups hg1 = create(Group1.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsConfiguredDontAcceptWithNoGroups() {
        // This is a bit complicated case: Should the result be true when exactly required is no groups
        // but no objects without groups are accepted? For now the implementation doesn't support this
        // and it should be rare to encounter this kind of situation so this is expected to return false.
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testExactlyRequiredTwoSameWrong() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group3.class, Group3.class);

        assertFalse( groupManager.match( hg1 ) );
    }



    /*
       Exactly tests
    */


    @Test
    public void testAtLeastRequired() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group2.class);

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredOneMoreThanNeeded() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group2.class, Group3.class);

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredOneMissing() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredOneWrong() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group3.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredTwoSame() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group1.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsConfiguredNoGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED );

        HasGroups hg1 = create();

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsConfiguredOneGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED );

        HasGroups hg1 = create(Group1.class);

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsConfiguredTwoGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED );

        HasGroups hg1 = create(Group1.class, Group2.class);

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsDontAccept() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsAcceptedAsDefault() {
        GenericGroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create();

        assertTrue( groupManager.match( hg1 ) );
    }
}
