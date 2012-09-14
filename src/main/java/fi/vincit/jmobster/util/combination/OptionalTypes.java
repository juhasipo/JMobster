package fi.vincit.jmobster.util.combination;

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

import fi.vincit.jmobster.util.collection.TypeCollection;

/**
 * Set of optional types.
 */
public class OptionalTypes extends TypeCollection<Class> {
    /**
     * Creates an OptionalTypes object with the given annotations
     * @param annotations Annotations to use as optional types
     * @return OptionalTypes object with given annotations
     */
    public static OptionalTypes get(Class... annotations) {
        return new OptionalTypes(annotations);
    }

    protected OptionalTypes(Class... annotations) {
        super(annotations);
    }
}
