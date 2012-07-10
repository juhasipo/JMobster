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

import java.lang.reflect.Array;

/**
 * <p>Base class for implementing different type of collections
 * which can be easily initialized and that can be easily distinguished
 * from one another.
 * </p>
 * <p>
 * Main purpose of this kind of class is to enable multiple
 * variable arguments in constructors and method calls. Instead of using plain
 * Java collections or lists, this uses shorter and prettier syntax and is supports
 * overloading better. For example there can be two methods with same name and take
 * different combinations of type collections, but since the type collections can be
 * subclassed, the method signatures don't conflict.
 * </p>
 * @param <T> Type of objects the collection holds
 */
public class TypeCollection<T> {
    private T[] types;
    public TypeCollection( T... types ) {
        this.types = types;
    }

    public T[] getTypes() {
        if( types != null ) {
            return types;
        } else {
            return (T[])Array.newInstance(types.getClass(), 0);
        }
    }
}
