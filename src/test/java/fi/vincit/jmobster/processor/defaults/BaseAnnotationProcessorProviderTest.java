package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseAnnotationProcessorProviderTest {
    private static class TestProvider extends BaseAnnotationProcessorProvider {
        private TestProvider( ValidationAnnotationProcessor... processors ) {
            super( processors );
        }

        @Override
        public void writeValidatorsForField( List<Annotation> annotations, ModelWriter writer ) {}
    }

    @Test
    public void testFindProcessor() {
        ValidationAnnotationProcessor processor = mockProcessor();
        Annotation annotation = mockAnnotation();

        TestProvider provider = new TestProvider(processor);

        ValidationAnnotationProcessor foundProcessor = provider.getBaseValidationProcessor(annotation);

        assertSame(processor, foundProcessor);
    }

    @Test
    public void testProcessorNotFound() {
        TestProvider provider = new TestProvider();

        Annotation annotation = mockAnnotation();

        ValidationAnnotationProcessor foundProcessor = provider.getBaseValidationProcessor(annotation);

        assertNull(foundProcessor);
    }

    private ValidationAnnotationProcessor mockProcessor() {
        ValidationAnnotationProcessor processor = mock(ValidationAnnotationProcessor.class);
        when(processor.isBaseValidator()).thenReturn(true);
        when(processor.getBaseValidatorForClass()).thenReturn(Annotation.class);
        return processor;
    }

    private Annotation mockAnnotation() {
        Annotation annotation = mock(Annotation.class);
        final Class annotationClass = Annotation.class;
        when(annotation.annotationType()).thenReturn(annotationClass);
        return annotation;
    }
}
