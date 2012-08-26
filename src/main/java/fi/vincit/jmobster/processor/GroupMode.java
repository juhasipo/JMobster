package fi.vincit.jmobster.processor;/*
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

/**
 * Configuration for setting how annotation grouping
 * is handled.
 */
public enum GroupMode {
    /**
     * If the annotation has any of the required groups
     */
    ANY_OF_REQUIRED,
    /**
     * Annotation has to have exactly the same groups
     * as required. Note: If you set includeValidatorsWithoutGroup
     * in GenericGroupManager to false and no groups are configured for
     * the group manager, no validators are accepted.
     */
    EXACTLY_REQUIRED,
    /**
     * Annotation has to have at least the required annotation
     * but may have more
     */
    AT_LEAST_REQUIRED
}
