package fi.vincit.jmobster.processor.frameworks.backbone;

import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.model.ModelField;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.writer.DataWriter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BackboneModelProcessorTest {
    @Test
    public void testProcessNoValidators() throws Exception {
        DataWriter writer = mockWriter();
        BackboneModelWriter backboneValueSectionWriter = mock(BackboneModelWriter.class);
        BackboneModelProcessor bmp = new BackboneModelProcessor(
                writer, "/**/", "ns", backboneValueSectionWriter
        );

        final List<ModelField> fields = new ArrayList<ModelField>();
        final Model testModel = new Model(String.class, "String", fields);
        testModel.setValidations(false);

        bmp.startProcessing();
        bmp.processModel(testModel, ItemStatus.inMiddle());
        bmp.endProcessing();
    }

    @Test
    public void testProcessWithValidators() throws Exception {
    }

    private DataWriter mockWriter() {
        DataWriter writer = mock(DataWriter.class);

        when(writer.indent()).thenReturn(writer);
        when(writer.write(anyString())).thenReturn(writer);
        when(writer.write(anyString(), anyString(), anyBoolean())).thenReturn(writer);
        when(writer.writeLine(anyString())).thenReturn(writer);
        when(writer.indentBack()).thenReturn(writer);
        when(writer.writeLine(anyString(), anyString(), anyBoolean())).thenReturn(writer);

        return writer;
    }

}
