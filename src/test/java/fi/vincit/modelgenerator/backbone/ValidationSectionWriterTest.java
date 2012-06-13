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
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ValidationSectionWriterTest {

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
        ap = new TestAnnotationProcessor();
    }

    @Test
    public void testOneField() throws Exception {
        ValidationSectionWriter testObject = new ValidationSectionWriter( modelWriter, ap);
        List<ModelField> modelFields = getFields(TestClassOneField.class);

        testObject.writeValidators( modelFields );
        modelWriter.close();

        assertEquals( "validate: {field: {TEST}}", stripLineChanges( os.toString() ) );
    }

    @Test
    public void testOneFieldNoAnnotations() throws Exception {
        ValidationSectionWriter testObject = new ValidationSectionWriter( modelWriter, ap);
        List<ModelField> modelFields = getFields(TestClassOneFieldNoValidation.class);

        testObject.writeValidators( modelFields );
        modelWriter.close();

        assertEquals( "validate: {field: {}}", stripLineChanges( os.toString() ) );
    }

    @Test
    public void testTwoFields() throws Exception {
        ValidationSectionWriter testObject = new ValidationSectionWriter( modelWriter, ap);
        List<ModelField> modelFields = getFields(TestClassTwoFields.class);

        testObject.writeValidators( modelFields );
        modelWriter.close();

        assertEquals( "validate: {field: {TEST},field2: {TEST}}", stripLineChanges( os.toString() ) );
    }

    /**
     * Strip line changes from string. In this test the line changes
     * don't matter. Those are tested in other tests. In these tests
     * only the textual data matters, like that there are correct
     * brackets and field names in correct position.
     * @param string String to strip from line changes
     * @return String without line changes
     */
    private String stripLineChanges( String string ) {
        return string.replace( "\n", "" ).replace("\r", "");
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
