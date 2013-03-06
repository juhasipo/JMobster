package fi.vincit.jmobster.util.test;

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

import java.io.Serializable;
import java.lang.reflect.Type;

public class TypeUtils {

    public static class WildCard<T extends Serializable> {
    }

    public static class WildCardImpl extends WildCard<Number> {
    }

    public static Type getNormalType() {
        return Long.class.getGenericSuperclass();
    }

    public static Type getParametrizedType() {
        return WildCardImpl.class.getGenericSuperclass();
    }

    public static Type getWildCardType() throws NoSuchMethodException {
        return TypeUtils.class.getDeclaredMethod("foo", WildCard.class).getGenericParameterTypes()[0];
    }

    // A method which is used to generate WildCardType
    private static void foo(WildCard<? extends Number> foo) {
    }
}
