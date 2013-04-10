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

    public static interface ModelFieldConfigurationImmutable {
        String getClasses();
        String getType();
        boolean useDefaultType();
        String getName();
    }

    private static class ModelFieldConfiguration implements ModelFieldConfigurationImmutable {
        private String type;
        private String classes;
        private String name;

        @Override
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        @Override
        public boolean useDefaultType() {
            return type == null;
        }

        @Override
        public String getName() {
            return name;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private Map<Class, ModelFieldConfigurations> modelConfigrations =
            new HashMap<Class, ModelFieldConfigurations>();

    private Class currentModel;
    private String currentField;
    private ModelFieldConfiguration modelField;

    public ModelConfiguration model(Class model) {
        this.currentModel = model;
        this.currentField = null;
        this.modelField = null;
        return this;
    }

    public ModelConfiguration field(String fieldName) {
        checkModelSet();
        this.currentField = fieldName;
        this.modelField = prepareSetFieldAttr();
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

    public ModelConfiguration name(String name) {
        ModelFieldConfiguration c2 = prepareSetFieldAttr();
        c2.setName(name);
        return this;
    }

    private ModelFieldConfiguration prepareSetFieldAttr() {
        checkModelSet();
        checkFieldNameSet();

        ModelFieldConfigurations c = getOrCreate(currentModel);
        if( this.modelField == null ) {
            this.modelField = c.getOrCreateConfiguration(currentField);
        }
        return this.modelField;
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

    public ModelFieldConfigurationImmutable getModelFieldConfiguration(Class model, String field) {
        if( hasConfiguration(model, field) ) {
            return modelConfigrations.get(model).modelFieldConfigurations.get(field);
        } else {
            return null;
        }
    }

    public boolean hasConfiguration(Class model, String field) {
        ModelFieldConfigurations c = modelConfigrations.get(model);
        return c != null && c.isConfigurationForField(field);
    }
}
