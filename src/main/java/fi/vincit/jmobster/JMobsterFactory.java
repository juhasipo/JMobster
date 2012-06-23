package fi.vincit.jmobster;

import fi.vincit.jmobster.exception.UnsupportedFramework;
import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.ModelProvider;
import fi.vincit.jmobster.processor.frameworks.backbone.DefaultAnnotationProcessorProvider;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.ConverterMode;
import fi.vincit.jmobster.processor.defaults.DefaultModelGenerator;
import fi.vincit.jmobster.processor.frameworks.backbone.BackboneModelProcessor;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.util.ModelWriter;

/**
 * <p>
 *     JMobsterFactory creates model generators for different frameworks. The factory
 *     configures the generators with appropriate processors by default.
 * </p>
 * <p>
 *     Currently the only supported framework is Backbone.js. The framework ID for it is
 *     "backbone.js" (also "backbone" works). The IDs are case insensitive.
 * </p>
 */
public class JMobsterFactory {
    /**
     * Creates a model generator instance for the given framework.
     * @param framework Framework ID
     * @param writer Writer to use
     * @return Configured model generator
     * @throws UnsupportedFramework If the framework is not supported
     */
    public static ModelGenerator getInstance(String framework, ModelWriter writer) {
        if( "backbone.js".equalsIgnoreCase(framework) || "backbone".equalsIgnoreCase(framework) ) {
            ModelProcessor modelProcessor = new BackboneModelProcessor(writer);
            AnnotationProcessorProvider annotationProcessorProvider = new DefaultAnnotationProcessorProvider();
            return new DefaultModelGenerator(
                    modelProcessor,
                    new JavaToJSValueConverter(ConverterMode.NULL_AS_DEFAULT),
                    annotationProcessorProvider);
        } else {
            throw new UnsupportedFramework("Framework " + framework + " not supported");
        }
    }

    public static ModelGenerator getInstance(String framework, ModelProvider provider) {
        return getInstance(framework, provider.getModelWriter());
    }
}
