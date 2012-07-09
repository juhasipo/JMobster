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

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Class manages required and optional class combinations.
 */
public class CombinationManager {
    private RequiredTypes requiredTypes;
    private OptionalTypes optionalTypes;

    private Map<Class, Class> requiredClasses;
    private Map<Class, Class> optionalClasses;

    /**
     * Constructs manager with only required types.
     * @param requiredTypes Required
     */
    public CombinationManager( RequiredTypes requiredTypes ) {
        this(requiredTypes, OptionalTypes.get());
    }

    /**
     * Constructs manager with required and optional types.
     * @param requiredTypes Required types
     * @param optionalTypes Optional types
     */
    public CombinationManager( RequiredTypes requiredTypes, OptionalTypes optionalTypes ) {
        this.requiredTypes = requiredTypes;
        this.optionalTypes = optionalTypes;

        this.requiredClasses = new HashMap<Class, Class>();
        for( Class c : this.requiredTypes.getTypes() ) {
            requiredClasses.put( c, c );
        }
        this.optionalClasses = new HashMap<Class, Class>();
        for( Class c : this.optionalTypes.getTypes() ) {
            optionalClasses.put( c, c );
        }
    }

    /**
     * Checks whether the managers combination matches
     * the given classes combination. The combination matches
     * if and only if the all of the given classes are same set
     * (same classes and count is same).
     * @param classes Classes to check
     * @return True if given classes match required classes, otherwise false.
     */
    public boolean matches( List<Annotation> classes ) {
        int matchesFound = 0;
        for( Annotation c : classes ) {
            if( requiredClasses.containsKey(c.annotationType()) ) {
                ++matchesFound;
            }
        }
        return matchesFound >= requiredClasses.size();
    }

    /**
     * Checks if the manager contains a class of given type.
     * @param type Type to find
     * @param <T> Class type
     * @return True if the manager contains the given class either as required or as an optional class. Otherwise false.
     */
    public <T> boolean containsClass(Class<T> type) {
        return requiredClasses.containsKey(type) || optionalClasses.containsKey(type);
    }

    /**
     * Finds the class with the given type. Always returns non-null
     * value when {@link CombinationManager#containsClass(Class)} returns true.
     * @param type Type to find
     * @param <T> Class type
     * @return Found class. If not found, null.
     */
    public <T> T findClass(Class<T> type) {
        if( requiredClasses.containsKey(type) ) {
            return (T)requiredClasses.get(type);
        } else if( optionalClasses.containsKey(type) ) {
            return (T)optionalClasses.get(type);
        } else {
            return null;
        }
    }
}
