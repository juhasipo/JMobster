package fi.vincit.jmobster.util;

import fi.vincit.jmobster.JMobsterFactory;
import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.writer.DataWriter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The purpose of model cache is to provide an easy way to generate and store generated
 * models dynamically by model name. One usage example could be a REST interface from which
 * validation rules could be queried. The interface could take the model name and give it
 * to a model cache. The cache then generates the model if needed or fetches it from the cache.
 * @param <C> {@link LanguageContext} to use
 * @param <W> {@link DataWriter} the LanguageContext uses
 */
public abstract class BaseModelCache<C extends LanguageContext<W>, W extends DataWriter> {
    private Map<String, String> generatedModelsByName = new HashMap<String, String>();

    private Map<String, Model> modelsByName = new HashMap<String, Model>();

    private ModelGenerator<W> modelGenerator;
    private ModelFactory modelFactory;
    private C languageContext;

    public BaseModelCache(ModelProcessor<C, W> modelProcessor, ModelFactory modelFactory) {
        this.modelGenerator = JMobsterFactory.getModelGenerator(modelProcessor);
        this.modelFactory = modelFactory;
        this.languageContext = modelProcessor.getLanguageContext();
    }

    protected C getLanguageContext() {
        return languageContext;
    }

    public String getModel(String name) {
        String internalModelName = nameToInternalName(name);
        if( !generatedModelsByName.containsKey(internalModelName) ) {
            if( !modelsByName.containsKey(internalModelName) ) {
                throw new IllegalArgumentException("No such model: " + name);
            }

            generateModelAndAddToCache(internalModelName);
        }
        return generatedModelsByName.get(internalModelName);
    }

    private void generateModelAndAddToCache(String internalModelName) {
        Model model = modelsByName.get(internalModelName);
        modelGenerator.process(model);
        String modelData = getLanguageContext().getWriter().toString();
        generatedModelsByName.put(internalModelName, modelData);
        getLanguageContext().getWriter().clear();
    }

    public void addModels(Collection<Class> classes) {
        Collection<Model> models = modelFactory.createAll(classes);
        for( Model model : models ) {
            String modelName = nameToInternalName(model.getName());
            modelsByName.put(modelName, model);
        }
    }

    private String nameToInternalName(String name) {
        return name;
    }

    public void clearModelCache() {
        generatedModelsByName.clear();
    }

    public void clearModelCacheAndModels() {
        clearModelCache();
        modelsByName.clear();
    }

    public Set<String> getModelNames() {
        return modelsByName.keySet();
    }
}
