package fi.vincit.jmobster.processor.defaults;

import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseFieldAnnotationWriterTest {
    private static class TestProviderField extends BaseFieldAnnotationWriter {
        private TestProviderField( ValidationAnnotationProcessor... processors ) {
            super( processors );
        }

        @Override
        public void writeValidatorsForField( ModelField field, ModelWriter writer ) {}
    }

    @Test
    public void testFindProcessor() {
        ValidationAnnotationProcessor processor = mockProcessor();
        Annotation annotation = mockAnnotation();

        TestProviderField provider = new TestProviderField(processor);

        ValidationAnnotationProcessor foundProcessor = provider.getBaseValidationProcessor(annotation);

        assertSame(processor, foundProcessor);
    }

    @Test
    public void testProcessorNotFound() {
        TestProviderField provider = new TestProviderField();

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
