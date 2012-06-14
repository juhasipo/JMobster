package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.ModelField;
import fi.vincit.modelgenerator.util.ModelWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultValueSectionWriterTest {
    private static class TestClassOneField {
        @Size(min = 1, max = 10)
        private String field;
    }

    private static class TestClassTwoFields {
        @Size(min = 1, max = 10)
        private String field;

        @Min(1)
        private long field2;
    }

    private static class TestClassOneFieldNoValidation {
        private String field;
    }

    private OutputStream os;
    private ModelWriter modelWriter;
    private AnnotationProcessor ap;

    @Before
    public void initModelWriter() {
        os = new ByteArrayOutputStream();
        modelWriter = new ModelWriter(os);
        modelWriter.setIndentation( 0 );
    }

    @Before
    public void initAnnotationProcessorStub() {
    }

    @Test
    public void testDefaultSectionWithNoFields() {
        DefaultValueSectionWriter d = new DefaultValueSectionWriter(modelWriter);
        List<ModelField> fields = Collections.EMPTY_LIST;

        d.writeDefaultValues(fields, false);
        modelWriter.close();

        assertEquals("defaults: function() {\nreturn {\n}\n}\n", os.toString());
    }

    @Test
    public void testDefaultSectionWithValidators() {
        DefaultValueSectionWriter d = new DefaultValueSectionWriter(modelWriter);
        List<ModelField> fields = getFields(TestClassOneField.class);
        fields.get(0).setDefaultValue("\"test default value\"");

        d.writeDefaultValues( fields, true );
        modelWriter.close();

        assertEquals("defaults: function() {\nreturn {\nfield: \"test default value\"\n}\n},\n", os.toString());
    }

    @Test
    public void testDefaultSectionWithoutValidators() {
        DefaultValueSectionWriter d = new DefaultValueSectionWriter(modelWriter);
        List<ModelField> fields = getFields(TestClassOneField.class);
        fields.get(0).setDefaultValue("\"test default value\"");

        d.writeDefaultValues(fields, false);
        modelWriter.close();

        assertEquals("defaults: function() {\nreturn {\nfield: \"test default value\"\n}\n}\n", os.toString());
    }

    @Test
    public void testDefaultSectionWithTwoFields() {
        DefaultValueSectionWriter d = new DefaultValueSectionWriter(modelWriter);
        List<ModelField> fields = getFields(TestClassTwoFields.class);
        fields.get(0).setDefaultValue("\"test default value\"");
        fields.get(1).setDefaultValue("1");

        d.writeDefaultValues(fields, false);
        modelWriter.close();

        assertEquals("defaults: function() {\nreturn {\nfield: \"test default value\",\nfield2: 1\n}\n}\n", os.toString());
    }


    /**
     * Constructs a list of test model fields.
     * @param testClass Test class from which to create the ModelFields
     * @return List of ModeField objects from the given test class
     */
    private List<ModelField> getFields(Class testClass) {
        List<ModelField> modelFields = new ArrayList<ModelField>();
        for( Field field : testClass.getDeclaredFields() ) {
            ModelField modelField = new ModelField(field, getAnnotation(field));
            modelFields.add( modelField );
        }
        return modelFields;
    }

    /**
     * Constructs a list of annotations.
     * @param field Field from which the annotations are get.
     * @return List of Annotations
     */
    private List<Annotation> getAnnotation(Field field) {
        List<Annotation> annotations = new ArrayList<Annotation>();
        for( Annotation annotation : field.getAnnotations() ) {
            annotations.add(annotation);
        }
        return annotations;
    }
}
