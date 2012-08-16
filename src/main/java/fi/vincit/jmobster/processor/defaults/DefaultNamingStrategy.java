package fi.vincit.jmobster.processor.defaults;
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

import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.model.Model;

/**
 * Default naming strategy for model names. Uses the model name (simple name)
 * but removes DTO (case insensitive) from the end of the model name.
 */
public class DefaultNamingStrategy implements ModelNamingStrategy {
    @Override
    public String getName( Class clazz ) {
        if( clazz.isAnnotationPresent(fi.vincit.jmobster.annotation.Model.class) ) {
            fi.vincit.jmobster.annotation.Model modelAnnotation =
                    (fi.vincit.jmobster.annotation.Model)clazz.getAnnotation(fi.vincit.jmobster.annotation.Model.class);
            return modelAnnotation.name();
        } else {
           return stripDtoFromName(clazz.getSimpleName());
        }
    }

    final static String DTO_POSTFIX = "dto";
    private String stripDtoFromName( String modelName ) {
        if( modelName.toLowerCase().endsWith(DTO_POSTFIX) ) {
            modelName = modelName.substring(0, modelName.length() - DTO_POSTFIX.length());
        }
        return modelName;
    }
}
