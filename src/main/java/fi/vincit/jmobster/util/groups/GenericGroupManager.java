package fi.vincit.jmobster.util.groups;

/*
 * Copyright 2012 Juha Siponen
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
import fi.vincit.jmobster.processor.GroupMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.Class;
import java.util.*;

/**
 * Default implementation of group manager. Function of this class is based on
 * breaking down the given group trees to list of leaves. So if the manager is set
 * to require a class which is a tree of interfaces, that class is broken down so that
 * the manager only contains the leaf interfaces. Also the input groups for match method
 * are broken down in similar way. These leaves are then used for the comparison which
 * makes checking different permutations much easier.
 */
public class GenericGroupManager implements GroupManager<Class> {

    private static final Logger LOG = LoggerFactory.getLogger( GenericGroupManager.class );

    final private Set<Class> requiredGroups;
    private GroupMode groupMode;
    private boolean includeValidatorsWithoutGroup = true;

    /**
     * Constructs a group manager
     * @param groupMode Group mode
     * @param requiredGroups Required groups
     */
    public GenericGroupManager( GroupMode groupMode, Class... requiredGroups ) {
        this( groupMode, Arrays.asList( requiredGroups ) );
    }

    /**
     * Constructs a group manager
     * @param groupMode Group mode
     * @param requiredGroups Required groups
     */
    public GenericGroupManager( GroupMode groupMode, Collection<Class> requiredGroups ) {
        this.requiredGroups = new HashSet<Class>(requiredGroups.size());
        setGroups( groupMode, requiredGroups );
    }

    /**
     * If set to true, the validators without group will be included in accepted groups.
     * Default value is true.
     * @param includeValidatorsWithoutGroup Value
     */
    public void setIncludeValidatorsWithoutGroup( boolean includeValidatorsWithoutGroup ) {
        this.includeValidatorsWithoutGroup = includeValidatorsWithoutGroup;
    }

    @Override
    public boolean match( HasGroups<Class> groupObject ) {
        if( groupObject.hasGroups() ) {
            return checkGroups( groupObject );
        } else if( !groupObject.hasGroups() && includeValidatorsWithoutGroup ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean match( Class[] groups ) {
        return false;
    }

    @Override
    public boolean match( Collection<Class> groups ) {
        return false;
    }

    @Override
    public void setGroups( GroupMode groupMode, Collection<Class> requiredGroups ) {
        this.groupMode = groupMode;
        this.requiredGroups.clear();
        // Break down groups to lowest level so that we only have leafs of the
        // group tree
        if( requiredGroups.size() > 0 ) {
            List<Class> groupTreeLeaves = breakDownGenericGroupsToLeaves( requiredGroups );
            this.requiredGroups.addAll( groupTreeLeaves );
        }
    }

    /**
     * Checks validator groups using the current groupMode.
     * @param groupObject Validator
     * @return True if groups match according to groupMode, otherwise false.
     */
    private boolean checkGroups(HasGroups<Class> groupObject) {
        Class[] groupsGiven = groupObject.getGroups();

        switch( groupMode ) {
            case ANY_OF_REQUIRED: return checkAnyRequiredGroups( groupsGiven );
            case EXACTLY_REQUIRED: return checkRequiredGroups( groupsGiven, CompareMode.Exactly );
            case AT_LEAST_REQUIRED: return checkRequiredGroups( groupsGiven, CompareMode.AtLeast );
            default: LOG.error("Invalid group mode: {}", groupMode); return false;
        }
    }

    private boolean checkAnyRequiredGroups( Class[] groupsGiven ) {
        // If no groups configured, this always passes
        if( requiredGroups.size() == 0 ) {
            return true;
        }

        for( Class groupToCheck : groupsGiven ) {
            if( this.requiredGroups.contains(groupToCheck) ) {
                return true;
            }
            for( Class requiredGroup : this.requiredGroups ) {
                if( checkMatchFromInterfaces( groupToCheck, requiredGroup ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recursive matcher for checking if the given group has an interface
     * that matches the given group.
     * @param groupWithInterfaces Group to check. The one that might have interfaces
     * @param requiredgroup Group to check against.
     * @return True if the given groupWithInterfaces has an interface that matches the given requiredgroup.
     */
    private boolean checkMatchFromInterfaces( Class groupWithInterfaces, Class requiredgroup ) {
        for( Class groupInterface : groupWithInterfaces.getInterfaces() ) {
            if( groupInterface.equals(requiredgroup) || checkMatchFromInterfaces( groupInterface, requiredgroup ) ) {
                return true;
            }
        }
        return false;
    }
    private int countNumberOfGroupsMatch( Set<Class> configuredGroups, Set<Class> givenGroups ) {
        int groupsFoundCount = 0;
        for( Class givenGroup : givenGroups ) {
            if( configuredGroups.contains(givenGroup) ) {
                ++groupsFoundCount;
            }
        }
        return groupsFoundCount;
    }

    private static enum CompareMode {
        Exactly,
        AtLeast
    }

    private boolean checkRequiredGroups( Class[] groupsGiven, CompareMode compareMode ) {
        // Then break down the input groups to leaf interface list
        // HashSet prevents duplicated groups
        final Set<Class> givenGroups = new HashSet<Class>( breakDownClassesGroupTreeLeaves(groupsGiven) );
        final int groupsGivenCount = givenGroups.size();

        // Here we check that the given count and needed count aren't unsuitable. With these tests
        // we can determine quite many cases that can't in any case make this method return true.
        // If these checks are not false, we can later just count the group count and check the equality
        // of group count.
        // When we do it in this order (first check given count and needed count and then count matches)
        // we can leave counting matches to the last possible moment.
        final int groupsNeededCount = requiredGroups.size();
        switch (compareMode) {
            case Exactly: if( groupsGivenCount != groupsNeededCount ) { return false; } break;
            case AtLeast: if( groupsGivenCount < groupsNeededCount ) { return false; } break;
        }

        // Then check that there are exact the required amount of groups
        // Above we checked that the number of groups is the same or less so we can be sure
        // that if the found equal number of group matches, the match is exact.
        int groupsFoundCount = countNumberOfGroupsMatch( requiredGroups, givenGroups );
        return groupsFoundCount == groupsNeededCount;
    }

    private void addLeavesToList(Class group, List groupTreeLeaves) {
        if( group.getInterfaces().length == 0 ) {
            groupTreeLeaves.add(group);
        } else {
            for( Class childGroup : group.getInterfaces() ) {
                addLeavesToList(childGroup, groupTreeLeaves);
            }
        }
    }

    private List<Class> breakDownClassesGroupTreeLeaves( Class[] groupsGiven ) {
        final List<Class> groupsToCheck = new ArrayList<Class>();
        if( groupsGiven.length > 0 ) {
            for( Class groupGiven : groupsGiven ) {
                addLeavesToList((Class)groupGiven, groupsToCheck);
            }
        }
        return groupsToCheck;
    }

    private List<Class> breakDownGenericGroupsToLeaves( Collection<Class> requiredGroups ) {
        final List<Class> groupTreeLeaves = new ArrayList<Class>();
        for( Class group : requiredGroups ) {
            addLeavesToList((Class)group, (List)groupTreeLeaves);
        }
        return groupTreeLeaves;
    }
}
