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

import fi.vincit.jmobster.processor.model.ModelField;

import java.util.List;

public interface FieldScanner {
    List<ModelField> getFields( Class clazz, FieldScanMode fieldScanMode );

    void setAllowStaticFields( boolean allowStaticFields );

    void setAllowFinalFields( boolean allowFinalFields );

    public static enum FieldScanMode {
        /**
         * Use getters. In this mode static fields cannot be used.
         */
        BEAN_PROPERTY,
        /**
         * Get public/protected/fields directly
         */
        DIRECT_FIELD_ACCESS
    }
}
