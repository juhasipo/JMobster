package fi.vincit.jmobster;

import fi.vincit.jmobster.exception.UnsupportedFramework;
import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.builder.ModelFactoryBuilder;
import fi.vincit.jmobster.processor.defaults.DefaultModelGenerator;
import fi.vincit.jmobster.processor.frameworks.backbone.ModelGeneratorBuilder;
import fi.vincit.jmobster.util.writer.DataWriter;

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
    private JMobsterFactory() {
    }

    public static ModelFactoryBuilder getModelFactoryBuilder() {
        return new ModelFactoryBuilder();
    }

    /**
     * Creates a model generator instance that is pre-configured for the given framework.
     * @param framework Framework ID
     * @param writer Writer to use
     * @return Configured model generator
     * @throws UnsupportedFramework If the framework is not supported
     */
    public static ModelGeneratorBuilder getModelGeneratorBuilder( String framework, DataWriter writer ) {
        if( "backbone.js".equalsIgnoreCase(framework) || "backbone".equalsIgnoreCase(framework) ) {
            return new ModelGeneratorBuilder().setDataWriter(writer);
        } else {
            throwFrameworkNotSupported( framework );
        }
        return null;
    }

    /**
     * Creates a model generator instance that is pre-configured for the given framework.
     * @param framework Framework ID
     * @param provider Model provider to use
     * @return Configured model generator
     * @throws UnsupportedFramework If the framework is not supported
     */
    public static ModelGeneratorBuilder getModelGeneratorBuilder( String framework, ModelProvider provider ) {
        return getModelGeneratorBuilder( framework, provider.getDataWriter() );
    }

    /**
     * Creates a model generator instance with custom {@link ModelProcessor}.
     * @param modelProcessor
     * @return
     */
    public static ModelGenerator getModelGenerator(ModelProcessor modelProcessor) {
        return new DefaultModelGenerator(modelProcessor);
    }

    private static ModelFactory throwFrameworkNotSupported( String framework ) {
        throw new UnsupportedFramework("Framework " + framework + " not supported");
    }
}
