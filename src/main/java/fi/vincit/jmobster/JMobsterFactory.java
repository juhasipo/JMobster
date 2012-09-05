package fi.vincit.jmobster;

import fi.vincit.jmobster.exception.UnsupportedFramework;
import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.builder.ModelFactoryBuilder;
import fi.vincit.jmobster.processor.builder.ModelGeneratorBuilder;
import fi.vincit.jmobster.processor.defaults.*;
import fi.vincit.jmobster.processor.defaults.validator.DefaultValidatorFactory;
import fi.vincit.jmobster.processor.languages.javascript.JavaToJSValueConverter;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.ConverterMode;
import fi.vincit.jmobster.processor.frameworks.backbone.BackboneModelProcessor;
import fi.vincit.jmobster.processor.languages.javascript.valueconverters.EnumConverter;
import fi.vincit.jmobster.util.groups.ClassGroupManager;
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

    public static ModelFactory getModelFactory() {
        ValidatorFactory validatorFactory = new DefaultValidatorFactory();
        ClassGroupManager groupManager = new ClassGroupManager(GroupMode.ANY_OF_REQUIRED);
        ValidatorScanner validatorScanner = new DefaultValidatorScanner(validatorFactory, groupManager);

        return new ModelFactoryBuilder()
                .setModelFieldFactory( new DefaultModelFieldFactory( FieldScanMode.DIRECT_FIELD_ACCESS, validatorScanner ) )
                .createDefaultModelFactory();
    }

    private static ModelFactory throwFrameworkNotSupported( String framework ) {
        throw new UnsupportedFramework("Framework " + framework + " not supported");
    }

    /**
     * Creates a model generator instance that is pre-configured for the given framework.
     * @param framework Framework ID
     * @param writer Writer to use
     * @return Configured model generator
     * @throws UnsupportedFramework If the framework is not supported
     */
    public static ModelGenerator getInstance(String framework, DataWriter writer) {
        if( "backbone.js".equalsIgnoreCase(framework) || "backbone".equalsIgnoreCase(framework) ) {
            FieldValueConverter valueConverter =
                    new JavaToJSValueConverter(
                            ConverterMode.NULL_AS_DEFAULT,
                            EnumConverter.EnumMode.STRING,
                            JavaToJSValueConverter.ISO_8601_DATE_TIME_TZ_PATTERN
                    );
            ModelProcessor modelProcessor = new BackboneModelProcessor(writer, valueConverter);

            return new ModelGeneratorBuilder()
                    .setModelProcessor( modelProcessor )
                    .createDefaultModelGenerator();
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
    public static ModelGenerator getInstance(String framework, ModelProvider provider) {
        return getInstance(framework, provider.getDataWriter());
    }
}
