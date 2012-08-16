package fi.vincit.jmobster.processor;

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

import fi.vincit.jmobster.processor.model.Validator;

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
    public boolean shouldAddValidator( Validator validator ) {
        if( validator.hasGroups() && checkGroups( validator ) ) {
            return true;
        } else if( !validator.hasGroups() && includeValidatorsWithoutGroup ) {
            return true;
        }
        return false;
    }

    /**
     * Checks validator groups using the current groupMode.
     * @param validator Validator
     * @return True if groups match according to groupMode, otherwise false.
     */
    private boolean checkGroups(Validator validator) {
        Class[] groupsInAnnotation = validator.getGroups();
        if( groupMode == GroupMode.ANY_OF_REQUIRED) {
            return checkAnyRequiredGroups( groupsInAnnotation );
        } else if( groupMode == GroupMode.AT_LEAST_REQUIRED) {
            return checkAtLeastRequiredGroups( groupsInAnnotation );
        } else if( groupMode == GroupMode.EXACTLY_REQUIRED) {
            return checkExactlyRequiredGroups( groupsInAnnotation );
        }
        return false;
    }

    private boolean checkAnyRequiredGroups( Class[] groupsInAnnotation ) {
        for( Class group : groupsInAnnotation ) {
            for( Class myGroup : this.groups ) {
                if( group.equals(myGroup) ) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: Test when given multiple same groups
    private boolean checkAtLeastRequiredGroups( Class[] groupsInAnnotation ) {
        final int groupsNeededCount = this.groups.length;
        if( groupsInAnnotation.length < groupsNeededCount ) {
            return false;
        }
        int groupsFoundCount = 0;
        for( Class aGroup : groupsInAnnotation ) {
            for( Class vGroup : this.groups ) {
                if( aGroup.equals(vGroup) ) {
                    ++groupsFoundCount;
                }
            }
        }
        return groupsFoundCount == groupsNeededCount;
    }

    private boolean checkExactlyRequiredGroups( Class[] groupsInAnnotation ) {
        final int groupsNeededCount = this.groups.length;
        if( groupsInAnnotation.length != groupsNeededCount ) {
            return false;
        }
        int groupsFoundCount = 0;
        for( Class aGroup : groupsInAnnotation ) {
            for( Class vGroup : this.groups ) {
                if( aGroup.equals(vGroup) ) {
                    ++groupsFoundCount;
                }
            }
        }
        return groupsFoundCount == groupsNeededCount;
    }
}
