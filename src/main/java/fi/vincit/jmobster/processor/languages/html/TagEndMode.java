package fi.vincit.jmobster.processor.languages.html;

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
 * How to end the current tag
 */
public enum TagEndMode {
    /**
     * Tag will be empty, close it right away (ends with /&gt)
     */
    EMPTY_TAG,
    /**
     * Tag will contain data or other tags (ends with /&gt).
     * You will need to end the tag later manually.
     */
    TAG_WITH_CONTENT
}