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

import fi.vincit.jmobster.processor.GroupMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Default implementation of group manager. Function of this class is based on
 * breaking down the given group trees to list of leaves. So if the manager is set
 * to require a class which is a tree of interfaces, that class is broken down so that
 * the manager only contains the leaf interfaces. Also the input groups for match method
 * are broken down in similar way. These leaves are then used for the comparison which
 * makes checking different permutations much easier.
 */
public class GenericGroupManager<T> implements GroupManager<T> {

    private static final Logger LOG = LoggerFactory.getLogger( GenericGroupManager.class );

    final private Collection<T> groups;
    private GroupMode groupMode;
    private boolean includeValidatorsWithoutGroup = true;

    /**
     * Constructs a group manager
     * @param groupMode Group mode
     * @param requiredGroups Required groups
     */
    public GenericGroupManager( GroupMode groupMode, T... requiredGroups ) {
        this(groupMode, Arrays.asList(requiredGroups));
    }

    /**
     * Constructs a group manager
     * @param groupMode Group mode
     * @param requiredGroups Required groups
     */
    public GenericGroupManager( GroupMode groupMode, Collection<T> requiredGroups ) {
        this.groups = new HashSet<T>(requiredGroups.size());
        setGroups(groupMode, requiredGroups);
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
    public boolean match( HasGroups<T> groupObject ) {
        if( groupObject.hasGroups() && checkGroups( groupObject ) ) {
            return true;
        } else if( !groupObject.hasGroups() && includeValidatorsWithoutGroup ) {
            return true;
        }
        return false;
    }

    @Override
    public void setGroups( GroupMode groupMode, Collection<T> requiredGroups ) {
        this.groupMode = groupMode;
        this.groups.clear();
        // Break down groups to lowest level so that we only have leafs of the
        // group tree
        if( requiredGroups.size() > 0 ) {
            List<T> groupTreeLeaves = breakDownGenericGroupsToLeaves( requiredGroups );
            this.groups.addAll(groupTreeLeaves);
        }
    }

    /**
     * Checks validator groups using the current groupMode.
     * @param groupObject Validator
     * @return True if groups match according to groupMode, otherwise false.
     */
    private boolean checkGroups(HasGroups<T> groupObject) {
        T[] groupsGiven = groupObject.getGroups();

        switch( groupMode ) {
            case ANY_OF_REQUIRED: return checkAnyRequiredGroups( groupsGiven );
            case EXACTLY_REQUIRED: return checkExactlyRequiredGroups( groupsGiven );
            case AT_LEAST_REQUIRED: return checkAtLeastRequiredGroups( groupsGiven );
            default: LOG.error("Invalid group mode: {}", groupMode); return false;
        }
    }

    private boolean checkAnyRequiredGroups( T[] groupsGiven ) {
        // If no groups configured, this always passes
        if( groups.size() == 0 ) {
            return true;
        }

        for( T group : groupsGiven ) {
            for( T myGroup : this.groups ) {
                if( group.equals(myGroup) ) {
                    return true;
                }

                // In case the group is a class, it may have interfaces that we also
                // should check to support advanced grouping like JSR-303 does
                if( group instanceof Class && checkMatchFromInterfaces( (Class)group, myGroup ) ) {
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
     * @param groupToCheck Group to check against.
     * @return True if the given groupWithInterfaces has an interface that matches the given groupToCheck.
     */
    private boolean checkMatchFromInterfaces( Class groupWithInterfaces, T groupToCheck ) {
        for( Class groupInterface : groupWithInterfaces.getInterfaces() ) {
            if( groupInterface.equals(groupToCheck) || checkMatchFromInterfaces( groupInterface, groupToCheck ) ) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAtLeastRequiredGroups( T[] groupsGiven ) {
        // First arrange the configured groups to a set so that we can
        // use contains method for fast checks later and no duplicates exist
        final Set<T> configuredGroups = new HashSet<T>(groups);
        final int groupsNeededCount = configuredGroups.size();

        // Then break down the input groups to leaf interface list
        // HashSet prevents duplicated groups
        final Set<T> givenGroups = new HashSet<T>( breakDownClassesGroupTreeLeaves( groupsGiven ));
        final int groupsGivenCount = givenGroups.size();

        // If given number of groups is less than the needed number of groups
        // then there is no way this method will return true
        if( groupsGivenCount < groupsNeededCount ) {
            return false;
        }

        // Then check that there are at least the required amount of groups
        int groupsFoundCount = countNumberOfGroupsMatch( configuredGroups, givenGroups );
        // Since we use HashSets as the data structure, it makes sure that we only
        // one instance of each group in use. This is why we can compare equality here.
        return groupsFoundCount == groupsNeededCount;
    }

    private int countNumberOfGroupsMatch( Set<T> configuredGroups, Set<T> givenGroups ) {
        int groupsFoundCount = 0;
        for( T givenGroup : givenGroups ) {
            if( configuredGroups.contains(givenGroup) ) {
                ++groupsFoundCount;
            }
        }
        return groupsFoundCount;
    }

    private boolean checkExactlyRequiredGroups( T[] groupsGiven ) {
        // First arrange the configured groups to a set so that we can
        // use contains method for fast checks later and no duplicates
        // exist
        final Set<T> configuredGroups = new HashSet<T>(groups);
        final int groupsNeededCount = configuredGroups.size();

        // Then break down the input groups to leaf interface list
        // HashSet prevents duplicated groups
        final Set<T> givenGroups = new HashSet<T>( breakDownClassesGroupTreeLeaves( groupsGiven ));
        final int groupsGivenCount = givenGroups.size();

        // If given number of groups is not equal to the needed number of groups
        // then there is no way this method will return true
        if( groupsGivenCount != groupsNeededCount ) {
            return false;
        }

        // Then check that there are exact the required amount of groups
        // Above we checked that the number of groups is the same so we can be sure
        // that if the found group count is equal, the match is exact.
        int groupsFoundCount = countNumberOfGroupsMatch( configuredGroups, givenGroups );
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

    private List<T> breakDownClassesGroupTreeLeaves( T[] groupsGiven ) {
        final List<T> groupsToCheck = new ArrayList<T>();
        if( groupsGiven.length > 0 ) {
            for( T groupGiven : groupsGiven ) {
                if( groupGiven instanceof Class ) {
                    addLeavesToList((Class)groupGiven, groupsToCheck);
                } else {
                    groupsToCheck.add(groupGiven);
                }
            }
        }
        return groupsToCheck;
    }

    private List<T> breakDownGenericGroupsToLeaves( Collection<T> requiredGroups ) {
        final List<T> groupTreeLeaves = new ArrayList<T>();
        for( T group : requiredGroups ) {
            if( group instanceof Class ) {
                addLeavesToList((Class)group, (List)groupTreeLeaves);
            } else {
                groupTreeLeaves.add(group);
            }
        }
        return groupTreeLeaves;
    }
}
