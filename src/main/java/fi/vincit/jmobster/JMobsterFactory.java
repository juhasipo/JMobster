package fi.vincit.jmobster;

import fi.vincit.jmobster.processor.frameworks.backbone.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.frameworks.backbone.DefaultAnnotationProcessorProvider;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.ConverterMode;
import fi.vincit.jmobster.processor.defaults.DefaultModelGenerator;
import fi.vincit.jmobster.processor.frameworks.backbone.BackboneModelProcessor;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.util.ModelWriter;

public class JMobsterFactory {
    public static ModelGenerator getInstance(String framework, ModelWriter writer) {
        if( "backbone.js".equalsIgnoreCase(framework) || "backbone".equalsIgnoreCase(framework) ) {
            ModelProcessor modelProcessor = new BackboneModelProcessor(writer);
            AnnotationProcessorProvider annotationProcessorProvider = new DefaultAnnotationProcessorProvider();
            return new DefaultModelGenerator(
                    modelProcessor,
                    new JavaToJSValueConverter(ConverterMode.NULL_AS_DEFAULT),
                    annotationProcessorProvider);
        } else {
            throw new IllegalArgumentException("Framework " + framework + "not supported");
        }
    }
}
