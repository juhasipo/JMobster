package fi.vincit.jmobster.processor.frameworks.backbone;

import fi.vincit.jmobster.processor.AnnotationProcessor;
import fi.vincit.jmobster.processor.ModelNamingStrategy;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ModelWriter;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BackboneModelProcessorTest {
    @Test
    public void testProcessNoValidators() throws Exception {
        ModelWriter writer = mockWriter();
        AnnotationProcessor annotationProcessor = mock(AnnotationProcessor.class);
        ModelNamingStrategy modelNamingStrategy = mock(ModelNamingStrategy.class);
        BackboneValueSectionWriter backboneValueSectionWriter = mock(BackboneValueSectionWriter.class);
        BackboneValidationSectionWriter backboneValidationSectionWriter = mock(BackboneValidationSectionWriter.class);
        BackboneModelProcessor bmp = new BackboneModelProcessor(
                writer, "", annotationProcessor, modelNamingStrategy, "/**/", "ns", backboneValueSectionWriter, backboneValidationSectionWriter
        );

        final List<ModelField> fields = new ArrayList<ModelField>();
        final Model testModel = new Model(String.class, fields);
        testModel.setValidations(false);

        bmp.startProcessing();
        bmp.processModel(testModel, false);
        bmp.endProcessing();

        InOrder order = inOrder(writer, annotationProcessor, modelNamingStrategy, backboneValueSectionWriter, backboneValidationSectionWriter );
        order.verify(modelNamingStrategy).getName(testModel);
        order.verify( backboneValueSectionWriter ).writeDefaultValues(fields, false);
        order.verify( backboneValidationSectionWriter, times(0)).writeValidators(any(List.class));
    }

    @Test
    public void testProcessWithValidators() throws Exception {
        ModelWriter writer = mockWriter();
        AnnotationProcessor annotationProcessor = mock(AnnotationProcessor.class);
        ModelNamingStrategy modelNamingStrategy = mock(ModelNamingStrategy.class);
        BackboneValueSectionWriter backboneValueSectionWriter = mock(BackboneValueSectionWriter.class);
        BackboneValidationSectionWriter backboneValidationSectionWriter = mock(BackboneValidationSectionWriter.class);
        BackboneModelProcessor bmp = new BackboneModelProcessor(
                writer, "", annotationProcessor, modelNamingStrategy, "/**/", "ns", backboneValueSectionWriter, backboneValidationSectionWriter
        );

        final List<ModelField> fields = new ArrayList<ModelField>();
        final Model testModel = new Model(String.class, fields);
        testModel.setValidations(true);

        bmp.startProcessing();
        bmp.processModel(testModel, true);
        bmp.endProcessing();

        InOrder order = inOrder(writer, annotationProcessor, modelNamingStrategy, backboneValueSectionWriter, backboneValidationSectionWriter );
        order.verify(modelNamingStrategy).getName(testModel);
        order.verify( backboneValueSectionWriter ).writeDefaultValues(fields, true);
        order.verify( backboneValidationSectionWriter ).writeValidators(fields);
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
