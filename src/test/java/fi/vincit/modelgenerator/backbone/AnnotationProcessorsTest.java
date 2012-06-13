package fi.vincit.modelgenerator.backbone;

import fi.vincit.modelgenerator.util.ModelWriter;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}
