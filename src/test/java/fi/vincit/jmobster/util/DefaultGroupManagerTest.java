package fi.vincit.jmobster.util;

import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.model.HasGroups;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultGroupManagerTest {

    public static interface Group1 {}
    public static interface Group2 {}
    public static interface Group3 {}

    private HasGroups create(Class... groups) {
        HasGroups hasGroups = Mockito.mock( HasGroups.class );
        final boolean groupsExist = groups.length > 0;
        Mockito.when( hasGroups.hasGroups() ).thenReturn( groupsExist );
        Mockito.when( hasGroups.getGroups() ).thenReturn( groups );
        return hasGroups;
    }

    @Test
    public void testAnyOfRequired() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class);
        HasGroups hg2 = create(Group2.class);
        HasGroups hg3 = create(Group1.class, Group2.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
        Assert.assertTrue( groupManager.shouldAddValidator( hg2 ) );
        Assert.assertTrue( groupManager.shouldAddValidator( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredNoGroupsConfigured() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.ANY_OF_REQUIRED );
        HasGroups hg1 = create();
        HasGroups hg2 = create(Group1.class);
        HasGroups hg3 = create(Group2.class);
        HasGroups hg4 = create(Group1.class, Group2.class);
        HasGroups hg5 = create(Group1.class, Group3.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
        Assert.assertTrue( groupManager.shouldAddValidator( hg2 ) );
        Assert.assertTrue( groupManager.shouldAddValidator( hg3 ) );
        Assert.assertTrue( groupManager.shouldAddValidator( hg4 ) );
        Assert.assertTrue( groupManager.shouldAddValidator( hg5 ) );
    }

    @Test
    public void testAnyOfRequiredNoGroupsDontAccept() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAnyOfRequiredNoGroupsAcceptAsDefault() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create();

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAnyOfRequiredWrongGroup() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group3.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }



    /*
        Exactly tests
     */


    @Test
    public void testExactlyRequired() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class, Group2.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredOneMissing() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredOneWrong() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class, Group3.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredAllWrong() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group3.class, String.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsDontAccept() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsAcceptAsDefault() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create();

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsConfigured() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED );
        HasGroups hg1 = create();

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsConfiguredTryGroups() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED );
        HasGroups hg1 = create(Group1.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredNoGroupsConfiguredDontAcceptWithNoGroups() {
        // This is a bit complicated case: Should the result be true when exactly required is no groups
        // but no objects without groups are accepted? For now the implementation doesn't support this
        // and it should be rare to encounter this kind of situation so this is expected to return false.
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testExactlyRequiredTwoSameWrong() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group3.class, Group3.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }



    /*
       Exactly tests
    */


    @Test
    public void testAtLeastRequired() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group2.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredOneMoreThanNeeded() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group2.class, Group3.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredOneMissing() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredOneWrong() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group3.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredTwoSame() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );

        HasGroups hg1 = create(Group1.class, Group1.class);

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsConfiguredNoGroups() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED );

        HasGroups hg1 = create();

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsConfiguredOneGroup() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED );

        HasGroups hg1 = create(Group1.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsConfiguredTwoGroups() {
        GroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED );

        HasGroups hg1 = create(Group1.class, Group2.class);

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsDontAccept() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );
        groupManager.setIncludeValidatorsWithoutGroup(false);
        HasGroups hg1 = create();

        Assert.assertFalse( groupManager.shouldAddValidator( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsAcceptedAsDefault() {
        DefaultGroupManager groupManager = new DefaultGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create();

        Assert.assertTrue( groupManager.shouldAddValidator( hg1 ) );
    }
}
