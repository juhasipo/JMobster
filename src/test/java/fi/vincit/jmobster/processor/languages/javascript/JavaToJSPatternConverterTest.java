package fi.vincit.jmobster.processor.languages.javascript;
/*
 * Copyright 2012 Juha Siponen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

import org.junit.Test;

import javax.validation.constraints.Pattern;

import static org.junit.Assert.assertEquals;

public class JavaToJSPatternConverterTest {
    @Test
    public void testPatternAnnotationProcessorSimple() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]");
        Assert.assertEquals( "/[\\dA-Z]/", result );
    }

    @Test
    public void testPatternAnnotationProcessorEmpty() {
        String result = JavaToJSPatternConverter.convertFromJava("");
        Assert.assertEquals( "//", result );
    }

    @Test
    public void testPatternAnnotationProcessorWhitespaceOnly() {
        String result = JavaToJSPatternConverter.convertFromJava("   \t   ");
        Assert.assertEquals( "//", result );
    }

    @Test(expected = AssertionError.class)
    public void testPatternAnnotationProcessorNull() {
        String result = JavaToJSPatternConverter.convertFromJava(null);
    }

    @Test(expected = AssertionError.class)
    public void testPatternAnnotationProcessorNullFlags() {
        Pattern.Flag[] flags = null;
        String result = JavaToJSPatternConverter.convertFromJava("", flags);
    }

    @Test
    public void testPatternAnnotationProcessorWithOneFlag() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]", Pattern.Flag.CASE_INSENSITIVE);
        Assert.assertEquals( "/[\\dA-Z]/i", result );
    }

    @Test
    public void testPatternAnnotationProcessorWithTwoFlags() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]", Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE);
        Assert.assertEquals( "/[\\dA-Z]/im", result );
    }

    @Test
    public void testPatternAnnotationProcessorWithOneUnsupportedFlag() {
        String result = JavaToJSPatternConverter.convertFromJava("[\\dA-Z]", Pattern.Flag.COMMENTS);
        Assert.assertEquals( "/[\\dA-Z]/", result );
    }

    @Test
    public void testPatternAnnotationProcessorWithTwoSuppoetedAndUnsupportedFlag() {
        String result = JavaToJSPatternConverter.convertFromJava(
                "[\\dA-Z]",
                Pattern.Flag.COMMENTS,
                Pattern.Flag.CASE_INSENSITIVE,
                Pattern.Flag.DOTALL,
                Pattern.Flag.MULTILINE);
        Assert.assertEquals( "/[\\dA-Z]/im", result );
    }
}
