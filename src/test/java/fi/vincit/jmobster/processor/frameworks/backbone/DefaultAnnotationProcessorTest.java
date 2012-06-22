package fi.vincit.jmobster.processor.frameworks.backbone;
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
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.SizeAnnotationProcessor;
import fi.vincit.jmobster.util.ModelWriter;
import fi.vincit.jmobster.util.StreamModelWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    public Min minWithTypeNumber;
    public Max maxWithTypeDecimal;

    @Before
    public void initTest() {
        os = new ByteArrayOutputStream();
        modelWriter = new StreamModelWriter(os);
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
        assertEquals("min\n", result);
    }

    @Test
    public void testWithMultipleProcessors() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, sizeAnnotation ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,\nmax,\nsize\n", result);
    }

    @Test
    public void testWithAnnotationWithoutProcessor() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, sizeAnnotationWithoutProcessor, maxAnnotation ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,\nmax\n", result);
    }

    @Test
    public void testWithType1() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minWithTypeNumber ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("type: \"number\",\nminNumber\n", result);
    }

    @Test
    public void testWithType2() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, minWithTypeNumber ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,\nmax,\ntype: \"number\",\nminNumber\n", result);
    }

    @Test
    public void testWithConflictingTypes() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, maxWithTypeDecimal, minWithTypeNumber ), modelWriter);
        modelWriter.close();

        String result = os.toString();
        assertEquals("min,\nmax,\ntype: \"decimal\",\nmaxDecimal,\nminNumber\n", result);
    }

    private AnnotationProcessorProvider mockAnnotationProcessorProvider() {
        // Create mock annotations
        minAnnotation = mock(Min.class);
        maxAnnotation = mock(Max.class);
        sizeAnnotation = mock(Size.class);
        sizeAnnotationWithoutProcessor = mock(Size.class);

        minWithTypeNumber = mock(Min.class);
        maxWithTypeDecimal = mock(Max.class);

        AnnotationProcessorProvider annotationProcessorProvider = mock(AnnotationProcessorProvider.class);

        // Mock annotation processors to write mock data to model writer
        MinAnnotationProcessor minProcessor = mock(MinAnnotationProcessor.class);
        when(minProcessor.requiresType()).thenReturn(false);
        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "min" );
                return true;
            }
        }).when( minProcessor ).writeValidatorsToStream( minAnnotation, modelWriter );

        MaxAnnotationProcessor maxProcessor = mock(MaxAnnotationProcessor.class);
        when(maxProcessor.requiresType()).thenReturn(false);
        doAnswer( new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "max" );
                return true;
            }
        }).when(maxProcessor).writeValidatorsToStream(maxAnnotation, modelWriter);

        SizeAnnotationProcessor sizeProcessor = mock(SizeAnnotationProcessor.class);
        when(sizeProcessor.requiresType()).thenReturn(false);
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "size" );
                return true;
            }
        }).when(sizeProcessor).writeValidatorsToStream( sizeAnnotation, modelWriter );


        MinAnnotationProcessor minWithTypeNumberProcessor = mock(MinAnnotationProcessor.class);
        when(minWithTypeNumberProcessor.requiresType()).thenReturn(true);
        when(minWithTypeNumberProcessor.requiredType()).thenReturn("number");
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "minNumber" );
                return true;
            }
        }).when(minWithTypeNumberProcessor).writeValidatorsToStream(minWithTypeNumber, modelWriter);

        MaxAnnotationProcessor maxWithTypeDecimalProcessor = mock(MaxAnnotationProcessor.class);
        when(maxWithTypeDecimalProcessor.requiresType()).thenReturn(true);
        when(maxWithTypeDecimalProcessor.requiredType()).thenReturn("decimal");
        doAnswer(new Answer() {
            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable {
                ((ModelWriter)invocation.getArguments()[1]).write( "maxDecimal" );
                return true;
            }
        }).when(maxWithTypeDecimalProcessor).writeValidatorsToStream(maxWithTypeDecimal, modelWriter);


        // Mock AnnotationProcessorProvider to return correct processor
        when(annotationProcessorProvider.getValidator(minAnnotation)).thenReturn( minProcessor );
        when(annotationProcessorProvider.getValidator(maxAnnotation)).thenReturn( maxProcessor );
        when(annotationProcessorProvider.getValidator(sizeAnnotation)).thenReturn( sizeProcessor );
        when(annotationProcessorProvider.getValidator(sizeAnnotationWithoutProcessor)).thenReturn( null );
        when(annotationProcessorProvider.getValidator(minWithTypeNumber)).thenReturn( minWithTypeNumberProcessor );
        when(annotationProcessorProvider.getValidator(maxWithTypeDecimal)).thenReturn( maxWithTypeDecimalProcessor );

        return annotationProcessorProvider;
    }
}
