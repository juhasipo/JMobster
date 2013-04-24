package fi.vincit.jmobster.processor.frameworks.configuration;

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

import java.util.HashMap;
import java.util.Map;

class ModelFieldConfigurations {
    Map<String, ModelFieldConfiguration> modelFieldConfigurations =
            new HashMap<String, ModelFieldConfiguration>();

    public ModelFieldConfiguration getConfiguration(String fieldName) {
        return modelFieldConfigurations.get(fieldName);
    }

    public boolean isConfigurationForField(String fieldName) {
        return modelFieldConfigurations.containsKey(fieldName);
    }

    public ModelFieldConfiguration getOrCreateConfiguration(String fieldName) {
        if( !isConfigurationForField(fieldName) ) {
            this.modelFieldConfigurations.put(fieldName, new ModelFieldConfiguration());
        }
        return getConfiguration(fieldName);
    }
}
