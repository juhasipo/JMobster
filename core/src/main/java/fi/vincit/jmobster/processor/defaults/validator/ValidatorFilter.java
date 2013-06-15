package fi.vincit.jmobster.processor.defaults.validator;

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

import fi.vincit.jmobster.processor.model.FieldAnnotation;
import fi.vincit.jmobster.util.groups.GenericGroupManager;
import fi.vincit.jmobster.util.groups.GroupManager;
import fi.vincit.jmobster.util.groups.GroupMode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Filters validators by group
 */
public class ValidatorFilter {

    private GroupManager<Class> groupManager;

    public ValidatorFilter(GroupMode groupMode, Class... groups) {
        this.groupManager = new GenericGroupManager(groupMode, groups);
    }

    public ValidatorFilter(GroupManager<Class> groupManager) {
        this.groupManager = groupManager;
    }

    public Collection<Annotation> filterByGroup(Collection<FieldAnnotation> annotations) {
        List<Annotation> filtered = new ArrayList<Annotation>(annotations.size());

        for( FieldAnnotation objectToFilter : annotations ) {
            if( groupManager.match( objectToFilter ) ) {
                filtered.add(objectToFilter.getAnnotation());
            }
        }
        return filtered;
    }
}
