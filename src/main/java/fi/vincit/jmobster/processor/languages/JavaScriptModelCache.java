package fi.vincit.jmobster.processor.languages;

import fi.vincit.jmobster.processor.ModelFactory;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptContext;
import fi.vincit.jmobster.processor.languages.javascript.writer.JavaScriptWriter;
import fi.vincit.jmobster.util.BaseModelCache;

public class JavaScriptModelCache extends BaseModelCache<JavaScriptContext, JavaScriptWriter> {
    public JavaScriptModelCache(ModelProcessor<JavaScriptContext, JavaScriptWriter> modelGenerator,
                                ModelFactory modelFactory) {
        super(modelGenerator, modelFactory);
    }
}
