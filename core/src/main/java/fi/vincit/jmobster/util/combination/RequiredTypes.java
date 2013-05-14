package fi.vincit.jmobster.util.combination;

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

import fi.vincit.jmobster.util.collection.TypeCollection;

/**
 * Set of required types.
 */
public class RequiredTypes extends TypeCollection<Class> {

    /**
     * Creates an RequiredTypes object with the given types
     * @param types Annotations to use as required types
     * @return RequiredTypes object with given types
     */
    public static RequiredTypes get(Class... types) {
        return new RequiredTypes(types);
    }

    protected RequiredTypes(Class... types) {
        super(types);
    }
}
