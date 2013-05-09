package fi.vincit.jmobster.util.groups;

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

import fi.vincit.jmobster.group.Default;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class GenericGroupManagerTest {

    public static interface Group1 {}
    public static interface Group2 {}
    public static interface Group3 {}

    public static interface Group1And2 extends Group1, Group2 {}
    public static interface Group1And2And3 extends Group1And2, Group3 {}

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

    @Test
    public void testExactlyNothingRequiredNothingGiven() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED );
        HasGroups hg1 = create();

        assertTrue( groupManager.match( hg1 ) );
    }



    /*
       At least tests
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

    @Test
    public void testAtLeastRequiredNoGroupsAcceptedGiveSameGroupTwice() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class );

        HasGroups hg1 = create(Group2.class, Group2.class);

        assertFalse( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsAcceptedGiveAllRequiredButSameGroupTwice() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class );

        HasGroups hg1 = create(Group1.class, Group2.class, Group2.class);

        assertTrue( groupManager.match( hg1 ) );
    }

    @Test
    public void testAtLeastRequiredNoGroupsAcceptedGiveCorrectGroupTwice() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class );

        HasGroups hg1 = create(Group1.class, Group1.class);

        assertTrue( groupManager.match( hg1 ) );
    }


    /**
     * Advanced multi-level inheritance tests
     */

    /**
     * Any of Required
     */

    @Test
    public void testAnyOfRequiredInheritanceBothInheritedGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group2.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredInheritanceOnlyOneInheritedGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class, Group3.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertTrue( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredInheritanceNoGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group3.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertFalse( groupManager.match( hg12 ) );
        assertTrue( groupManager.match( hg3 ) );
    }


    @Test
    public void testAnyOfRequiredMultiLevelInheritanceOnlyToInheritedGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredMultiLevelInheritanceOnlyOneBottomLevelGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group3.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertTrue( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredMultiLevelInheritanceOnlyOneMiddleLevelGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group2.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }


    @Test
    public void testAnyOfRequiredInheritanceInheritedMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1And2.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredInheritanceInheritedPartialMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1And2.class );
        HasGroups hg1 = create(Group1.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg1 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredInheritanceInheritedPartialMatchAll() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Group1And2And3.class );
        HasGroups hg12 = create(Group1.class, Group2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertTrue( groupManager.match( hg3 ) );
    }

    /**
     * Exactly Required
     */

    @Test
    public void testExactlyRequiredInheritanceExactMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group2.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritanceNoMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class, Group3.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertFalse( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }



    @Test
    public void testExactlyRequiredInheritancePartialMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertFalse( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritancePartialMatch2() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1And2.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritancePartialMatch3() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1And2.class );
        HasGroups hg12 = create(Group1.class, Group2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritancePartialMatch4() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1And2And3.class );
        HasGroups hg123 = create(Group1.class, Group2.class, Group3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritancePartialMatch5() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1And2And3.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritancePartialMatchOverlappingGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1And2And3.class, Group1And2.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testExactlyRequiredInheritancePartialMatchTwoSameGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.EXACTLY_REQUIRED, Group1And2And3.class, Group1And2And3.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }


    /**
     * At Least Required
     */

    @Test
    public void testAtLeastRequiredInheritanceExactMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritancePartialMatch() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritanceInheritedAsRequired() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1And2.class );
        HasGroups hg12 = create(Group1.class, Group2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritanceInheritedAsRequiredAndGiven() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1And2.class );
        HasGroups hg12 = create(Group1And2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg12 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritanceInheritedAsRequiredAndGivenMoreThanRequired() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1And2.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritanceInheritedAsRequiredMoreThanRequiredGiven() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1And2.class );
        HasGroups hg123 = create(Group1.class, Group2.class, Group3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritanceMultilevelInheritanceRequired() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1And2And3.class );
        HasGroups hg123 = create(Group1.class, Group3.class, Group2.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredInheritanceMultilevelInheritanceRequiredAdnGiven() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1And2And3.class );
        HasGroups hg123 = create(Group1And2And3.class);
        HasGroups hg3 = create(Group3.class);

        assertTrue( groupManager.match( hg123 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    /**
     * Default group
     */

    public interface HasDefault extends Default {
    }
    public interface NoDefault {
    }

    @Test
    public void testAnyOfRequiredWithDefaultGroup1() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED );
        HasGroups hg1 = create(HasDefault.class);
        HasGroups hg3 = create(NoDefault.class);

        assertTrue( groupManager.match( hg1 ) );
        assertTrue( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredWithDefaultGroup2() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, HasDefault.class );
        HasGroups hg1 = create(HasDefault.class);
        HasGroups hg3 = create(NoDefault.class);

        assertTrue( groupManager.match( hg1 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAnyOfRequiredWithDefaultGroup3() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, Default.class );
        HasGroups hg1 = create(HasDefault.class);
        HasGroups hg3 = create(NoDefault.class);

        assertTrue( groupManager.match( hg1 ) );
        assertFalse( groupManager.match( hg3 ) );
    }

    @Test
    public void testAtLeastRequiredWithDefaultGroup() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.ANY_OF_REQUIRED, NoDefault.class );
        HasGroups hg1 = create(HasDefault.class);
        HasGroups hg3 = create(NoDefault.class);

        assertFalse( groupManager.match( hg1 ) );
        assertTrue( groupManager.match( hg3 ) );
    }

    /*
     Other
    */

    @Test
    public void testChangeModeAndGroups() {
        GroupManager groupManager = new GenericGroupManager( GroupMode.AT_LEAST_REQUIRED, Group1.class, Group2.class );
        HasGroups hg1 = create(Group1.class, Group2.class, Group3.class);
        assertTrue( groupManager.match( hg1 ) );

        groupManager.setGroups(GroupMode.EXACTLY_REQUIRED, TestUtil.collectionFromObjects(Group1.class, Group2.class));
        assertFalse( groupManager.match( hg1 ) );
    }

}
