package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.processor.GroupManager;
import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.model.HasGroups;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation of group manager
 */
public class DefaultGroupManager implements GroupManager {

    final private Class[] groups;
    final private GroupMode groupMode;
    private boolean includeValidatorsWithoutGroup = true;

    /**
     * Constructs a group manager
     * @param groupMode Group mode
     * @param requiredGroups Required groups
     */
    public DefaultGroupManager( GroupMode groupMode, Class... requiredGroups ) {
        this.groupMode = groupMode;
        this.groups = requiredGroups;
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
    public boolean shouldAddValidator( HasGroups groupObject ) {
        if( groupObject.hasGroups() && checkGroups( groupObject ) ) {
            return true;
        } else if( !groupObject.hasGroups() && includeValidatorsWithoutGroup ) {
            return true;
        }
        return false;
    }

    /**
     * Checks validator groups using the current groupMode.
     * @param groupObject Validator
     * @return True if groups match according to groupMode, otherwise false.
     */
    private boolean checkGroups(HasGroups groupObject) {
        Class[] groupsGiven = groupObject.getGroups();

        if( groupMode == GroupMode.ANY_OF_REQUIRED) {
            return checkAnyRequiredGroups( groupsGiven );
        } else if( groupMode == GroupMode.AT_LEAST_REQUIRED) {
            return checkAtLeastRequiredGroups( groupsGiven );
        } else if( groupMode == GroupMode.EXACTLY_REQUIRED) {
            return checkExactlyRequiredGroups( groupsGiven );
        }
        return false;
    }

    private boolean checkAnyRequiredGroups( Class[] groupsGiven ) {
        // If no groups configured, this always passes
        if( groups.length == 0 ) {
            return true;
        }

        for( Class group : groupsGiven ) {
            for( Class myGroup : this.groups ) {
                if( group.equals(myGroup) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAtLeastRequiredGroups( Class[] groupsGiven ) {
        final Set<Class> configuredGroups = new HashSet<Class>(groups.length);
        for( Class group : groups ) {
            configuredGroups.add( group );
        }
        final int groupsNeededCount = configuredGroups.size();

        final Set<Class> givenGroups = new HashSet<Class>(groupsGiven.length);
        for( Class group : groupsGiven ) {
            givenGroups.add( group );
        }
        final int groupsGivenCount = givenGroups.size();

        if( groupsGivenCount < groupsNeededCount ) {
            return false;
        }

        int groupsFoundCount = 0;
        for( Class givenGroup : givenGroups ) {
            if( configuredGroups.contains(givenGroup) ) {
                ++groupsFoundCount;
            }
        }
        return groupsFoundCount == groupsNeededCount;
    }

    private boolean checkExactlyRequiredGroups( Class[] groupsGiven ) {
        // If no groups configured, this always passes
        if( groups.length == 0 && groupsGiven.length == 0 ) {
            return true;
        }

        final int groupsNeededCount = this.groups.length;
        if( groupsGiven.length != groupsNeededCount ) {
            return false;
        }
        int groupsFoundCount = 0;
        for( Class aGroup : groupsGiven ) {
            for( Class vGroup : this.groups ) {
                if( aGroup.equals(vGroup) ) {
                    ++groupsFoundCount;
                }
            }
        }
        return groupsFoundCount == groupsNeededCount;
    }
}
