package fi.vincit.jmobster.processor.frameworks.backbone;

import fi.vincit.jmobster.processor.ValidationAnnotationProcessor;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static fi.vincit.jmobster.util.TestUtil.getField;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class BackboneAnnotationProcessorProviderTest {

    @Test
    public void testProcessOneAnnotation() {
        ModelWriter writer = mockWriter();
        ValidationAnnotationProcessor processor = mock(ValidationAnnotationProcessor.class);
        final Annotation annotation = mock(Annotation.class);
        ArgumentMatcher<ModelField> argumentMatcher = new ArgumentMatcher<ModelField>() {
            @Override
            public boolean matches( Object argument ) {
                ModelField field = (ModelField)argument;
                List<Annotation> la = ((ModelField) argument).getAnnotations();
                return la.get(0) == annotation;
            }
        };
        when(processor.canProcess(argThat(argumentMatcher))).thenReturn(true);
        BackboneFieldAnnotationWriter dapp = new BackboneFieldAnnotationWriter(processor);

        List<Annotation> annotationsToTest = new ArrayList<Annotation>();
        annotationsToTest.add( annotation );
        dapp.writeValidatorsForField( getField(annotationsToTest), writer );

        verify(processor, times(1)).writeValidatorsToStream(any(ModelField.class), eq(writer));
    }

    @Test
    public void testProcessOneAnnotationWithType() {
        ModelWriter writer = mockWriter();
        List<Annotation> annotationsToTest = new ArrayList<Annotation>();
        ValidationAnnotationProcessor processor = mockProcessor("number", annotationsToTest);
        BackboneFieldAnnotationWriter dapp = new BackboneFieldAnnotationWriter(processor);

        dapp.writeValidatorsForField( getField(annotationsToTest), writer );

        verify(processor, times(1)).writeValidatorsToStream(any(ModelField.class), eq(writer));
        verify(writer).write( eq( "number" ) );
    }

    @Test
    public void testProcessOneAnnotationWithTwoSameTypes() {
        ModelWriter writer = mockWriter();
        List<Annotation> annotationsToTest = new ArrayList<Annotation>();
        ValidationAnnotationProcessor processor1 = mockProcessor("number", annotationsToTest);
        ValidationAnnotationProcessor processor2 = mockProcessor("number", annotationsToTest);

        BackboneFieldAnnotationWriter dapp = new BackboneFieldAnnotationWriter(processor1, processor2);

        dapp.writeValidatorsForField( getField(annotationsToTest), writer );

        verify(processor1, times(1)).writeValidatorsToStream(any(ModelField.class), eq(writer));
        verify(processor2, times(1)).writeValidatorsToStream(any(ModelField.class), eq(writer));
        verify(writer, times(1)).write(eq("number"));
    }

    @Test
    public void testProcessOneAnnotationWithTwoDifferentTypes() {
        ModelWriter writer = mockWriter();
        List<Annotation> annotationsToTest = new ArrayList<Annotation>();
        ValidationAnnotationProcessor processor1 = mockProcessor("date", annotationsToTest);
        ValidationAnnotationProcessor processor2 = mockProcessor("number", annotationsToTest);

        BackboneFieldAnnotationWriter dapp = new BackboneFieldAnnotationWriter(processor1, processor2);

        dapp.writeValidatorsForField( getField(annotationsToTest), writer );

        verify(processor1, times(1)).writeValidatorsToStream(any(ModelField.class), eq(writer));
        verify(processor2, times(1)).writeValidatorsToStream(any(ModelField.class), eq(writer));
        verify(writer, times(1)).write(eq("date"));
    }


    private ValidationAnnotationProcessor mockProcessor(List<Annotation> annotations) {
        return mockProcessor(null, annotations);
    }

    private ValidationAnnotationProcessor mockProcessor(String annotationType, List<Annotation> annotations) {
        ValidationAnnotationProcessor processor = mock(ValidationAnnotationProcessor.class);
        if( annotationType != null ) {
            when(processor.requiredType()).thenReturn(annotationType);
            when(processor.requiresType()).thenReturn(true);
        }
        final int annotationInList = annotations.size();
        final Annotation annotation = mock(Annotation.class);
        ArgumentMatcher<ModelField> argumentMatcher = new ArgumentMatcher<ModelField>() {
            @Override
            public boolean matches( Object argument ) {
                ModelField field = (ModelField)argument;
                List<Annotation> la = field.getAnnotations();
                return la.get(annotationInList) == annotation;
            }
        };
        when(processor.canProcess(argThat(argumentMatcher))).thenReturn(true);

        annotations.add(annotation);
        return processor;
    }

    private ModelWriter mockWriter() {
        ModelWriter writer = mock(ModelWriter.class);

        when(writer.indent()).thenReturn(writer);
        when(writer.write(anyString())).thenReturn(writer);
        when(writer.write(anyString(), anyString(), anyBoolean())).thenReturn(writer);
        when(writer.writeLine(anyString())).thenReturn(writer);
        when(writer.indentBack()).thenReturn(writer);
        when(writer.writeLine(anyString(), anyString(), anyBoolean())).thenReturn(writer);

        return writer;
    }
}
