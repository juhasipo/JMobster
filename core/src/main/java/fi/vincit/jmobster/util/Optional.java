package fi.vincit.jmobster.util;

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
 * Represents optional argument. Main use is to prevent returning
 * and passing null values. Check if the value is present with
 * {@link fi.vincit.jmobster.util.Optional#isPresent()} and get the value
 * with {@link fi.vincit.jmobster.util.Optional#getValue()} method.
 *
 * If the user calls {@link fi.vincit.jmobster.util.Optional#getValue()}
 * an exception is thrown.
 *
 * @param <T> Type the optional object contains.
 */
public class Optional<T> {

    private static Optional EMPTY_VALUE = new Optional(null);
    public static Optional empty() {
        return EMPTY_VALUE;
    }

    private T value;

    public Optional(T value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T getValue() {
        if( value == null ) {
            throw new IllegalStateException("Value is null");
        }
        return value;
    }
}
