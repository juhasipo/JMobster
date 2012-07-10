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

/**
 * Class to mark a set of classes as required types.
 */
public class RequiredTypes extends TypeCollection<Class> {

    /**
     * Creates an RequiredTypes object with the given annotations
     * @param annotations Annotations to use as required types
     * @return RequiredTypes object with given annotations
     */
    public static RequiredTypes get(Class... annotations) {
        return new RequiredTypes(annotations);
    }

    protected RequiredTypes(Class... annotations) {
        super(annotations);
    }
}
