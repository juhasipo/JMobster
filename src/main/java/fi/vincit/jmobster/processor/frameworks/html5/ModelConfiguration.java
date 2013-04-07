package fi.vincit.jmobster.processor.frameworks.html5;

import java.util.HashMap;
import java.util.Map;

public class ModelConfiguration {
    private static class ModelFieldConfigurations {
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

    private static class ModelFieldConfiguration {
        private String type;
        private String classes;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }
    }

    private Map<Class, ModelFieldConfigurations> modelConfigrations =
            new HashMap<Class, ModelFieldConfigurations>();

    private Class currentModel;
    private String currentField;

    public ModelConfiguration model(Class model) {
        this.currentModel = model;
        this.currentField = null;
        return this;
    }

    public ModelConfiguration field(String fieldName) {
        checkModelSet();
        this.currentField = fieldName;
        return this;
    }

    public ModelConfiguration type(String type) {
        ModelFieldConfiguration c2 = prepareSetFieldAttr();
        c2.setType(type);
        return this;
    }

    public ModelConfiguration classes(String classes) {
        ModelFieldConfiguration c2 = prepareSetFieldAttr();
        c2.setClasses(classes);
        return this;
    }

    private ModelFieldConfiguration prepareSetFieldAttr() {
        checkModelSet();
        checkFieldNameSet();

        ModelFieldConfigurations c = getOrCreate(currentModel);
        return c.getOrCreateConfiguration(currentField);
    }

    private void checkModelSet() {
        if( currentModel == null ) {
            throw new IllegalStateException();
        }
    }

    private void checkFieldNameSet() {
        if( currentField == null ) {
            throw new IllegalStateException();
        }
    }

    private ModelFieldConfigurations getOrCreate(Class clazz) {
        if( !modelConfigrations.containsKey(clazz) ) {
            modelConfigrations.put(clazz, new ModelFieldConfigurations());
        }
        return modelConfigrations.get(clazz);
    }
}
