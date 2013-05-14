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
 * Describes how null values should be handled.
 */
public enum ConverterMode {
    /**
     * Allow nulls as they are. E.g. null string will have JS value null
     * if the given value is null.
     */
    ALLOW_NULL,
    /**
     * Null values are replaced with default value. E.g. null Long will
     * be replaced with 0 and null string with "".
     */
    NULL_AS_DEFAULT
}
