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

public class DefaultNamingStrategy implements ModelNamingStrategy {
    @Override
    public String getName( Model model ) {
        Class modelClass = model.getModelClass();
        if( modelClass.isAnnotationPresent(fi.vincit.jmobster.annotation.Model.class) ) {
            fi.vincit.jmobster.annotation.Model modelAnnotation =
                    (fi.vincit.jmobster.annotation.Model)modelClass.getAnnotation(fi.vincit.jmobster.annotation.Model.class);
            return modelAnnotation.name();
        } else {
           return stripDtoFromName(modelClass.getSimpleName());
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
