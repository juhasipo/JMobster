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

/**
 * Tells that object has groups. Can be used with {@link fi.vincit.jmobster.util.groups.GroupManager}
 * ubt be sure to make the group type match.
 * @param <T> Type of group
 */
public interface HasGroups<T> {
    T[] getGroups();
    boolean hasGroups();
}
