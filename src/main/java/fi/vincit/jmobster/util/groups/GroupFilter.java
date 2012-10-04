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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that filters given classes by using the given {@link GroupManager}
 * @param <T> Class that implements {@link HasGroups} HasGroups type has to be same as Group type given to this class
 *           and the given {@link GroupManager} uses.
 * @param <G> Group type the {@link GroupManager} uses.
 */
public class GroupFilter<T extends HasGroups<G>, G> {
    private final GroupManager<G> groupManager;
    public GroupFilter(GroupManager<G> groupManager) {
        this.groupManager = groupManager;
    }

    /**
     * Filter given objects using the current configuration
     * @param objectsToFilter Objects to filter
     * @return Filtered groups. If nothing left after filtering, an empty collection.
     */
    public Collection<T> filterByGroups(Collection<T> objectsToFilter) {
        List<T> filtered = new ArrayList<T>(objectsToFilter.size());

        for( T objectToFilter : objectsToFilter ) {
            if( groupManager.match( objectToFilter ) ) {
                filtered.add(objectToFilter);
            }
        }

        return filtered;
    }

    /**
     * Set group filtering configuration
     * @param groupMode Group mode
     * @param groups Groups to use for filtering
     */
    public void setFilterGroups( GroupMode groupMode, Collection<G> groups ) {
        groupManager.setGroups( groupMode, groups );
    }
}
