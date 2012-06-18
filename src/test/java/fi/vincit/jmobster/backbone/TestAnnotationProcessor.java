package fi.vincit.jmobster.backbone;
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

import fi.vincit.jmobster.util.ModelWriter;

import java.lang.annotation.Annotation;
import java.util.List;

public class TestAnnotationProcessor implements AnnotationProcessor {
    @Override
    public void writeValidation( List<Annotation> validationAnnotations, ModelWriter writer ) {
        if( validationAnnotations.size() > 0 ) {
            writer.write("TEST");
        }
    }
}