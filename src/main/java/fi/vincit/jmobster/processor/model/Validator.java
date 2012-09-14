package fi.vincit.jmobster.processor.model;
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

import fi.vincit.jmobster.util.collection.AnnotationBag;

/**
 * Interface for validators
 */
public interface Validator extends HasType {
    /**
     * Initializes the validator. {@link AnnotationBag} given
     * as parameter always contains required annotations and
     * may contain optional annotations.
     * @param annotationBag Annotation bag
     */
    void init(AnnotationBag annotationBag);
}
