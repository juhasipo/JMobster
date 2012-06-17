package fi.vincit.jmobster.backbone;

import fi.vincit.jmobster.ModelField;
import fi.vincit.jmobster.util.ItemProcessor;
import fi.vincit.jmobster.util.ModelWriter;

import java.util.List;

public class ValidationSectionWriter {
    private ModelWriter writer;
    private AnnotationProcessor annotationProcessor;

    public ValidationSectionWriter( ModelWriter writer, AnnotationProcessor annotationProcessor ) {
        this.writer = writer;
        this.annotationProcessor = annotationProcessor;
    }

    public void writeValidators( List<ModelField> fields ) {
        writer.writeLine("validate: {").indent();
        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) {
                writer.write(field.getField().getName()).writeLine( ": {" ).indent();
                annotationProcessor.writeValidation(field.getAnnotations(), writer);
                writer.indentBack();
                writer.writeLine( "}", ",", !isLastItem );
            }
        };
        modelFieldItemProcessor.process(fields);
        writer.indentBack();
        writer.writeLine( "}" );
    }
}
