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

public class CombinationManager {
    private RequiredTypes requiredTypes;
    private OptionalTypes optionalTypes;

    private Map<Class, Class> requiredClasses;
    private Map<Class, Class> optionalClasses;

    public CombinationManager( RequiredTypes requiredTypes ) {
        this(requiredTypes, OptionalTypes.get());
    }

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

    public boolean supports(List<Annotation> classes) {
        int matchesFound = 0;
        for( Annotation c : classes ) {
            if( requiredClasses.containsKey(c.annotationType()) ) {
                ++matchesFound;
            }
        }
        return matchesFound >= requiredClasses.size();
    }

    public <T> boolean containsClass(Class<T> type) {
        return requiredClasses.containsKey(type) || optionalClasses.containsKey(type);
    }

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
