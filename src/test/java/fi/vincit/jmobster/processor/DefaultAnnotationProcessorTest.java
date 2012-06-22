package fi.vincit.jmobster.processor;
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

import fi.vincit.jmobster.processor.frameworks.backbone.annotation.MaxAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.MinAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.PatternAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.SizeAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.StreamModelWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultAnnotationProcessorTest {

    private OutputStream os;
    private ModelWriter modelWriter;

    public Min minAnnotation;
    public Max maxAnnotation;
    public Size sizeAnnotation;
    public Size sizeAnnotationWithoutProcessor;
    public Min minWithTypeNumberAnnotation;
    public Max maxWithTypeDecimalAnnotation;
    public Size sizeWithOneGroupAnnotation;
    public Pattern patternWithTwoGroupsAnnotation;

    @Before
    public void initTest() {
        os = new ByteArrayOutputStream();
        modelWriter = new StreamModelWriter(os);
        modelWriter.setLineSeparator("");
    }

    private static <T>List<T> listFromObjects(T... objects) {
        List<T> arrayList = new ArrayList<T>(objects.length);
        for( T t : objects ) {
            arrayList.add(t);
        }
        return arrayList;
    }

    @Test
    public void testWithNoProcessors() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects(), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("", result);
    }

    @Test
    public void testWithOneProcessor() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min", result);
    }

    @Test
    public void testWithMultipleProcessors() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, sizeAnnotation ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,max,size", result);
    }

    @Test
    public void testWithAnnotationWithoutProcessor() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, sizeAnnotationWithoutProcessor, maxAnnotation ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,max", result);
    }

    @Test
    public void testWithType1() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects(minWithTypeNumberAnnotation), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("type: \"number\",minNumber", result);
    }

    @Test
    public void testWithType2() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, minWithTypeNumberAnnotation), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,max,type: \"number\",minNumber", result);
    }

    @Test
    public void testWithConflictingTypes() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, maxWithTypeDecimalAnnotation, minWithTypeNumberAnnotation), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,max,type: \"decimal\",maxDecimal,minNumber", result);
    }


    @Test
    public void testGroupsWithAnyMode() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.ANY_OF_REQUIRED, TestGroup1.class, TestGroup2.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("pattern,size", result);
    }

    @Test
    public void testGroupsWithExactMode() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("pattern", result);
    }

    @Test
    public void testGroupsWithExactModeGroupedNotLast() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("pattern", result);
    }


    @Test
    public void testGroupsWithAnyModeIncludeNoGrouped() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.ANY_OF_REQUIRED, TestGroup1.class, TestGroup2.class);
        dap.setIncludeValidationsWithoutGroup(true);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation), modelWriter);

        modelWriter.close();

        String result = os.toString();
        assertEquals("min,size,pattern", result);
    }

    @Test
    public void testGroupsWithExactModeIncludeNoGrouped() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class);
        dap.setIncludeValidationsWithoutGroup(true);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        modelWriter.close();

        String result = os.toString();
        assertEquals("min,size,pattern", result);
    }

    @Test
    public void testGroupsWithExactModeWithNotEnoughGroups() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class, TestGroup3.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        modelWriter.close();

        String result = os.toString();
        assertEquals("", result);
    }

    @Test
    public void testGroupsWithExactModeWithTooManyhGroups() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.EXACTLY_REQUIRED, TestGroup1.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        modelWriter.close();

        String result = os.toString();
        assertEquals("size", result);
    }

    @Test
    public void testGroupsWithAtLeastMode() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, DefaultAnnotationProcessor.GroupMode.AT_LEAST_REQUIRED, TestGroup1.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        modelWriter.close();

        String result = os.toString();
        assertEquals("pattern,size", result);
    }


    private AnnotationProcessorProvider mockAnnotationProcessorProvider() {
        AnnotationProcessorProvider annotationProcessorProvider = mock(AnnotationProcessorProvider.class);

        sizeAnnotationWithoutProcessor = mock(Size.class);
        when(annotationProcessorProvider.getValidator(sizeAnnotationWithoutProcessor)).thenReturn( null );

        // Mock annotation processors to write mock data to model writer
        minAnnotation = mock(Min.class);
        MinAnnotationProcessor minProcessor = mock(MinAnnotationProcessor.class);
        when(minProcessor.getGroups(minAnnotation)).thenReturn(new Class[]{});
        when(minProcessor.hasGroups(minAnnotation)).thenReturn(false);
        when(minProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(minAnnotation)).thenReturn( minProcessor );
        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "min" );
                return true;
            }
        }).when( minProcessor ).writeValidatorsToStream( minAnnotation, modelWriter );


        maxAnnotation = mock(Max.class);
        MaxAnnotationProcessor maxProcessor = mock(MaxAnnotationProcessor.class);
        when(maxProcessor.getGroups(maxAnnotation)).thenReturn(new Class[]{});
        when(maxProcessor.hasGroups(maxAnnotation)).thenReturn(false);
        when(maxProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(maxAnnotation)).thenReturn( maxProcessor );
        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "max" );
                return true;
            }
        }).when(maxProcessor).writeValidatorsToStream(maxAnnotation, modelWriter);


        sizeAnnotation = mock(Size.class);
        SizeAnnotationProcessor sizeProcessor = mock(SizeAnnotationProcessor.class);
        when(sizeProcessor.getGroups(sizeAnnotation)).thenReturn(new Class[]{});
        when(sizeProcessor.hasGroups(sizeAnnotation)).thenReturn(false);
        when(sizeProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(sizeAnnotation)).thenReturn( sizeProcessor );
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "size" );
                return true;
            }
        }).when(sizeProcessor).writeValidatorsToStream( sizeAnnotation, modelWriter );


        minWithTypeNumberAnnotation = mock(Min.class);
        MinAnnotationProcessor minWithTypeNumberProcessor = mock(MinAnnotationProcessor.class);
        when(minWithTypeNumberProcessor.getGroups(minWithTypeNumberAnnotation)).thenReturn(new Class[]{});
        when(minWithTypeNumberProcessor.hasGroups(minWithTypeNumberAnnotation)).thenReturn(false);
        when(minWithTypeNumberProcessor.requiresType()).thenReturn(true);
        when(minWithTypeNumberProcessor.requiredType()).thenReturn("number");
        when(annotationProcessorProvider.getValidator(minWithTypeNumberAnnotation)).thenReturn( minWithTypeNumberProcessor );
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "minNumber" );
                return true;
            }
        }).when(minWithTypeNumberProcessor).writeValidatorsToStream(minWithTypeNumberAnnotation, modelWriter);


        maxWithTypeDecimalAnnotation = mock(Max.class);
        MaxAnnotationProcessor maxWithTypeDecimalProcessor = mock(MaxAnnotationProcessor.class);
        when(maxWithTypeDecimalProcessor.getGroups(maxWithTypeDecimalAnnotation)).thenReturn(new Class[]{});
        when(maxWithTypeDecimalProcessor.hasGroups(maxWithTypeDecimalAnnotation)).thenReturn(false);
        when(maxWithTypeDecimalProcessor.requiresType()).thenReturn(true);
        when(maxWithTypeDecimalProcessor.requiredType()).thenReturn("decimal");
        when(annotationProcessorProvider.getValidator(maxWithTypeDecimalAnnotation)).thenReturn( maxWithTypeDecimalProcessor );
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "maxDecimal" );
                return true;
            }
        }).when(maxWithTypeDecimalProcessor).writeValidatorsToStream(maxWithTypeDecimalAnnotation, modelWriter);


        sizeWithOneGroupAnnotation = mock(Size.class);
        SizeAnnotationProcessor sizeWithOneGroupProcessor = mock(SizeAnnotationProcessor.class);
        when(sizeWithOneGroupProcessor.getGroups(sizeWithOneGroupAnnotation)).thenReturn(new Class[]{TestGroup1.class});
        when(sizeWithOneGroupProcessor.hasGroups(sizeWithOneGroupAnnotation)).thenReturn(true);
        when(sizeWithOneGroupProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(sizeWithOneGroupAnnotation)).thenReturn( sizeWithOneGroupProcessor );
        when(sizeWithOneGroupAnnotation.groups()).thenReturn(new Class[]{TestGroup1.class});
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "size" );
                return true;
            }
        }).when(sizeWithOneGroupProcessor).writeValidatorsToStream(sizeWithOneGroupAnnotation, modelWriter);


        patternWithTwoGroupsAnnotation = mock(Pattern.class);
        PatternAnnotationProcessor patternWithTwoGroupsProcessor = mock(PatternAnnotationProcessor.class);
        when(patternWithTwoGroupsProcessor.getGroups(patternWithTwoGroupsAnnotation)).thenReturn(new Class[]{TestGroup1.class, TestGroup2.class});
        when(patternWithTwoGroupsProcessor.hasGroups(patternWithTwoGroupsAnnotation)).thenReturn(true);
        when(patternWithTwoGroupsProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(patternWithTwoGroupsAnnotation)).thenReturn( patternWithTwoGroupsProcessor );
        when(patternWithTwoGroupsAnnotation.groups()).thenReturn(new Class[]{TestGroup1.class, TestGroup2.class});
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "pattern" );
                return true;
            }
        }).when(patternWithTwoGroupsProcessor).writeValidatorsToStream(patternWithTwoGroupsAnnotation, modelWriter);

        return annotationProcessorProvider;
    }

    public static class TestGroup1 {};
    public static class TestGroup2 {};
    public static class TestGroup3 {};
}
