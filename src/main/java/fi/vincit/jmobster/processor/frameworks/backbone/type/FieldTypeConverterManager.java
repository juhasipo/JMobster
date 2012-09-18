package fi.vincit.jmobster.processor.frameworks.backbone.type;

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

import fi.vincit.jmobster.processor.model.ModelField;

import java.util.HashMap;
import java.util.Map;

public class FieldTypeConverterManager {
    private final Map<Class, FieldTypeConverter> fieldTypeConvertersByClass;

    public FieldTypeConverterManager() {
        fieldTypeConvertersByClass = new HashMap<Class, FieldTypeConverter>();
    }

    public String getType(ModelField field) {
        if( fieldTypeConvertersByClass.containsKey(field.getFieldType()) ) {
            FieldTypeConverter converter = fieldTypeConvertersByClass.get(field.getFieldType());
            return converter.convert(field);
        } else {
            return "";
        }
    }

    public void setFieldTypeConverter(FieldTypeConverter... fieldTypeConverters) {
        for( FieldTypeConverter fieldTypeConverter : fieldTypeConverters ) {
            for( Class supportedType : fieldTypeConverter.getSupportedTypes() ) {
                fieldTypeConvertersByClass.put(supportedType, fieldTypeConverter );
            }
        }
    }
}
