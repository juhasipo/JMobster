package fi.vincit.jmobster.util;

/*
 * Copyright 2012-2013 Juha Siponen
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

import fi.vincit.jmobster.JMobsterFactory;
import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.groups.GroupMode;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The purpose of model cache is to provide an easy way to generate and store generated
 * models dynamically by model name. One usage example could be a REST interface from which
 * validation rules could be queried. The interface could take the model name and give it
 * to a model cache. The cache then generates the model if needed or fetches it from the cache.
 * @param <C> {@link LanguageContext} to use
 * @param <W> {@link DataWriter} the LanguageContext uses
 */
public abstract class BaseModelCache<C extends LanguageContext<W>, W extends DataWriter> {

    static class ModelIdentity {
        public String name;
        public Set<Class> groups;
        public Class[] groupArray;

        public ModelIdentity(String name, Set<Class> groups) {
            this.name = name;
            this.groups = groups;
            // TODO Optimize array creation
            groupArray = new Class[groups.size()];
            int i = 0;
            for( Class group : groups ) {
                groupArray[i] = group;
                ++i;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ModelIdentity)) return false;

            ModelIdentity that = (ModelIdentity) o;
            return groups.equals(that.groups) && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + groups.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return name + ":" + groups;
        }
    }

    private Map<ModelIdentity, String> generatedModelsByName = new ConcurrentHashMap<ModelIdentity, String>();
    private Map<String, Model> modelsByName = new ConcurrentHashMap<String, Model>();
    private Map<String, Class> groupDirectory = new ConcurrentHashMap<String, Class>();

    private ModelGenerator<W> modelGenerator;
    private ModelFactory modelFactory;
    private ModelProcessor<C,W> modelProcessor;
    private C languageContext;

    public BaseModelCache(ModelProcessor<C, W> modelProcessor, ModelFactory modelFactory) {
        this.modelGenerator = JMobsterFactory.getModelGenerator(modelProcessor);
        this.modelProcessor = modelProcessor;
        this.modelFactory = modelFactory;
        this.languageContext = modelProcessor.getLanguageContext();
    }

    protected C getLanguageContext() {
        return languageContext;
    }

    public void addGroup(Class group, String groupName) {
        groupDirectory.put(groupName, group);
    }

    public String getModelByNameAndGroupNames(String name, String... groups) {
        Set<Class> groupSet = groupNamesToClasses(groups);
        return getModelInternal(new ModelIdentity(name, groupSet));
    }

    private String getModelInternal(ModelIdentity internalModelName) {
        if( !generatedModelsByName.containsKey(internalModelName) ) {
            if( !modelsByName.containsKey(internalModelName.name) ) {
                throw new IllegalArgumentException("No such model: " + internalModelName);
            }

            generateModelAndAddToCache(internalModelName);
        }
        return generatedModelsByName.get(internalModelName);
    }

    private Set<Class> groupNamesToClasses(String... groups) {
        Set<Class> groupClasses = new HashSet<Class>();
        for( String group : groups ) {
            if( groupDirectory.containsKey(group) ) {
                groupClasses.add(groupDirectory.get(group));
            } else {
                throw new RuntimeException("Could not map " + group + " to a group");
            }
        }
        return groupClasses;
    }

    public String getModelByNameAndGroupClasses(String name, Class... groups) {
        return getModelInternal(nameToInternalName(name, groups));
    }

    private synchronized void generateModelAndAddToCache(ModelIdentity modelIdentity) {
        Model model = modelsByName.get(modelIdentity.name);
        assert(model != null);
        modelProcessor.setValidatorFilter(GroupMode.ANY_OF_REQUIRED, modelIdentity.groupArray);
        modelGenerator.process(model);
        String modelData = getLanguageContext().getWriter().toString();
        generatedModelsByName.put(modelIdentity, modelData);
        getLanguageContext().getWriter().clear();
    }

    public void addModels(Collection<Class> classes) {
        Collection<Model> models = modelFactory.createAll(classes);
        for( Model model : models ) {
            ModelIdentity modelName = nameToInternalName(model.getName());
            modelsByName.put(modelName.name, model);
        }
    }

    private ModelIdentity nameToInternalName(String name, Class... groups) {
        Set<Class> groupSet = new HashSet<Class>(groups.length);
        Collections.addAll(groupSet, groups);
        return new ModelIdentity(name, groupSet);
    }

    public void clearModelCache() {
        generatedModelsByName.clear();
    }

    public void clearModelCacheAndModels() {
        clearModelCache();
        modelsByName.clear();
    }

    public Set<String> getModelNames() {
        Set<String> names = new TreeSet<String>();
        for( String identity : modelsByName.keySet() ) {
            names.add(identity);
        }
        return names;
    }
}
