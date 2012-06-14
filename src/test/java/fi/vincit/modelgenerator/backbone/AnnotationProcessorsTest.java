package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.util.ModelWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class AnnotationProcessorsTest {

    private OutputStream os;
    private ModelWriter modelWriter;

    @Before
    public void initModelWriter() {
        os = new ByteArrayOutputStream();
        modelWriter = new ModelWriter(os);
        modelWriter.setIndentation( 0 );
    }

    @Test
    public void testMinAnnotationProcessor() {
        class T { @Min(1) int i; }
        MinAnnotationProcessor m = new MinAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals( "min: 1", os.toString() );
    }

    @Test
    public void testMaxAnnotationProcessor() {
        class T { @Max(100) int i; }
        MaxAnnotationProcessor m = new MaxAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("max: 100", os.toString());
    }

    @Test
    public void testPatternAnnotationProcessorSimple() {
        class T { @Pattern(regexp = "[\\dA-Z]") int i; }
        PatternAnnotationProcessor m = new PatternAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("pattern: /[\\dA-Z]/", os.toString());
    }

    @Test
    public void testSizeAnnotationProcessorSimple() {
        class T { @Size(min = 1, max = 255) String s; }
        SizeAnnotationProcessor m = new SizeAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("minlength: 1,\nmaxlength: 255", os.toString());
    }

    @Test
    public void testNotNullAnnotationProcessorSimple() {
        class T { @NotNull String s; }
        NotNullAnnotationProcessor m = new NotNullAnnotationProcessor();
        m.writeValidatorsToStream(T.class.getDeclaredFields()[0].getAnnotations()[0], modelWriter);
        modelWriter.close();
        assertEquals("required: true", os.toString());
    }
}
