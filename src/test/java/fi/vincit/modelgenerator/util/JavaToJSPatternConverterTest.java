package fi.vincit.modelgenerator.util;

import fi.vincit.modelgenerator.backbone.annotation.PatternAnnotationProcessor;
import org.junit.Test;

import javax.validation.constraints.Pattern;

import static org.junit.Assert.assertEquals;

public class JavaToJSPatternConverterTest {
    @Test
    public void testPatternAnnotationProcessorSimple() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]");
        assertEquals( "/[\\dA-Z]/", result );
    }

    @Test
    public void testPatternAnnotationProcessorWithOneFlag() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]", Pattern.Flag.CASE_INSENSITIVE);
        assertEquals("/[\\dA-Z]/i", result);
    }

    @Test
    public void testPatternAnnotationProcessorWithTwoFlags() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]", Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE);
        assertEquals("/[\\dA-Z]/im", result);
    }

    @Test
    public void testPatternAnnotationProcessorWithOneUnsupportedFlag() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]", Pattern.Flag.COMMENTS);
        assertEquals("/[\\dA-Z]/", result);
    }

    @Test
    public void testPatternAnnotationProcessorWithTwoSuppoetedAndUnsupportedFlag() {
        String result = JavaToJSPatternConverter.convertFromJava(
                "[\\dA-Z]",
                Pattern.Flag.COMMENTS,
                Pattern.Flag.CASE_INSENSITIVE,
                Pattern.Flag.DOTALL,
                Pattern.Flag.MULTILINE);
        assertEquals("/[\\dA-Z]/im", result);
    }
}
