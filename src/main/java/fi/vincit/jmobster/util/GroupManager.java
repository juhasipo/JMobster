package fi.vincit.jmobster.util;

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

import fi.vincit.jmobster.processor.model.HasGroups;

/**
 * GroupManager handles checking whether a given validator
 * contains the required groups to be included in a model field.
 */
public interface GroupManager {
    /**
     * Checks if the validator has the required groups
     * @param groupObject Object with groups
     * @return True if groups match, otherwise false
     */
    boolean shouldAddValidator( HasGroups groupObject );
}
