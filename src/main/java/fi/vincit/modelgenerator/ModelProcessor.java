package fi.vincit.modelgenerator;

import fi.vincit.modelgenerator.backbone.AnnotationProcessor;
import fi.vincit.modelgenerator.util.ItemProcessor;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

public class ModelProcessor {
    private static final Logger LOG = LoggerFactory
            .getLogger( ModelProcessor.class );

    private ModelWriter writer;

    private String modelFilePath;

    public ModelProcessor( String modelFilePath ) {
        this.modelFilePath = modelFilePath;
    }

    public void startProcessing() throws IOException {
        writer = new ModelWriter(modelFilePath);
        writer.open();

        writer.writeLine("/*\n" +
                " * Auto-generated file\n" +
                " */\n" +
                "var Models = {");
        writer.indent();
    }

    public void processModel(Model model, boolean isLastModel) throws IOException {
        String modelName = model.getModelClass().getSimpleName();

        writer.write(modelName).writeLine( ": Backbone.Model.extend({" ).indent();

        writeDefaultValues(model.getFields(), model.hasValidations());
        writeValidators(model.getFields());

        writer.indentBack();
        if( isLastModel ) {
            writer.writeLine("})");
        } else {
            writer.writeLine("}),");
        }
    }

    private void writeValidators( List<ModelField> fields ) throws IOException {
        writer.writeLine("validate: {").indent();
        final AnnotationProcessor annotationProcessor = new AnnotationProcessor();
        final ItemProcessor<Annotation> annotationItemProcessor = new ItemProcessor<Annotation>() {
            @Override
            protected void process( Annotation annotation, boolean isLastItem ) throws IOException {

                if( isLastItem ) {
                    writer.writeLine("");
                } else {
                    writer.writeLine(",");
                }
            }
        };
        for( ModelField field : fields ) {
            writer.write(field.getField().getName()).write(": {").indent();
            annotationProcessor.writeValidation(field.getAnnotations(), writer);
            writer.indentBack();
            writer.writeLine("}");
        }
    }


    private void writeDefaultValues( List<ModelField> fields, boolean hasValidators ) throws IOException {
        writer.writeLine( "defaults: function() {" ).indent();
        writer.writeLine("return {").indent();
        final ItemProcessor<ModelField> modelFieldItemProcessor = new ItemProcessor<ModelField>() {
            @Override
            protected void process( ModelField field, boolean isLastItem ) throws IOException {
                writer.write(field.getField().getName()).write(": ").write(field.getDefaultValue());
                if( isLastItem ) {
                    writer.writeLine("");
                } else {
                    writer.writeLine(",");
                }
            }
        };
        modelFieldItemProcessor.process( fields, modelFieldItemProcessor );
        writer.writeLine("}").indentBack();
        if( hasValidators ) {
            writer.writeLine("},");
        } else {
            writer.writeLine("}");
        }

    }

    public void endProcessing() throws IOException {
        writer.writeLine("}");
        writer.indentBack();
        writer.close();
    }
}
