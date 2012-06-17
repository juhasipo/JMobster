package fi.vincit.jmobster.backbone;

import fi.vincit.jmobster.ModelField;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import java.util.List;

public class DefaultValueSectionWriter {
    private ModelWriter writer;

    public DefaultValueSectionWriter( ModelWriter writer ) {
        this.writer = writer;
    }

    public void writeDefaultValues( List<ModelField> fields, boolean hasValidators ) {
        writer.writeLine( "defaults: function() {" ).indent();
        writer.writeLine("return {").indent();
        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) {
                writer.write(field.getField().getName()).write(": ").write(field.getDefaultValue());
                writer.writeLine("", ",", !isLastItem);
            }
        };
        modelFieldItemProcessor.process( fields );
        writer.indentBack();
        writer.writeLine("}").indentBack();
        writer.writeLine("}", ",", hasValidators);
    }
}
