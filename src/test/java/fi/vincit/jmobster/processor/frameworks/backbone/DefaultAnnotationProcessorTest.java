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

import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.GroupMode;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.MaxAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.MinAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.PatternAnnotationProcessor;
import fi.vincit.jmobster.processor.frameworks.backbone.annotation.SizeAnnotationProcessor;
import fi.vincit.jmobster.processor.languages.javascript.JavaScriptWriter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultAnnotationProcessorTest {

    private OutputStream os;
    private JavaScriptWriter modelWriter;

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
        modelWriter = mock(JavaScriptWriter.class);
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
        verify(app).process( matchAnnotationList(), eq(modelWriter));
    }

    @Test
    public void testWithOneProcessor() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation ), modelWriter);

        verify(app).process( matchAnnotationList( minAnnotation ), eq(modelWriter));
    }

    @Test
    public void testWithMultipleProcessors() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, maxAnnotation, sizeAnnotation ), modelWriter);

        verify(app).process( matchAnnotationList( minAnnotation, maxAnnotation, sizeAnnotation ), eq(modelWriter));
    }

    @Test
    public void testWithAnnotationWithoutProcessor() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app);

        dap.writeValidation((List)listFromObjects( minAnnotation, sizeAnnotationWithoutProcessor, maxAnnotation ), modelWriter);

        verify(app).process( matchAnnotationList( minAnnotation, maxAnnotation ), eq(modelWriter));
    }


    @Test
    public void testGroupsWithAnyMode() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.ANY_OF_REQUIRED, TestGroup1.class, TestGroup2.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation ), eq(modelWriter));
    }

    @Test
    public void testGroupsWithExactMode() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( patternWithTwoGroupsAnnotation ), eq(modelWriter));
    }

    @Test
    public void testGroupsWithExactModeGroupedNotLast() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( patternWithTwoGroupsAnnotation ), eq(modelWriter));
    }


    @Test
    public void testGroupsWithAnyModeIncludeNoGrouped() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.ANY_OF_REQUIRED, TestGroup1.class, TestGroup2.class);
        dap.setIncludeValidationsWithoutGroup(true);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation ), eq(modelWriter));
    }

    @Test
    public void testGroupsWithExactModeIncludeNoGrouped() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class);
        dap.setIncludeValidationsWithoutGroup(true);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation ), eq(modelWriter));
    }

    @Test
    public void testGroupsWithExactModeWithNotEnoughGroups() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.EXACTLY_REQUIRED, TestGroup1.class, TestGroup2.class, TestGroup3.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList(), eq(modelWriter));
    }

    @Test
    public void testGroupsWithExactModeWithTooManyGroups() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.EXACTLY_REQUIRED, TestGroup1.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( sizeWithOneGroupAnnotation ), eq(modelWriter));
    }

    @Test
    public void testGroupsWithAtLeastMode() {
        AnnotationProcessorProvider app = mockAnnotationProcessorProvider();
        DefaultAnnotationProcessor dap = new DefaultAnnotationProcessor(app, GroupMode.AT_LEAST_REQUIRED, TestGroup1.class);

        dap.writeValidation((List)listFromObjects(minAnnotation, sizeAnnotation, patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation), modelWriter);

        verify(app).process( matchAnnotationList( patternWithTwoGroupsAnnotation, sizeWithOneGroupAnnotation ), eq(modelWriter));
    }

    private List<Annotation> matchAnnotationList( Annotation... annotationsToFind ) {
        final Set<Annotation> annotationsToFindSet = new HashSet<Annotation>();
        for( Annotation a : annotationsToFind ) {
            annotationsToFindSet.add( a );
        }

        ArgumentMatcher<List<Annotation>> argumentMatcher = new ArgumentMatcher<List<Annotation>>() {
            @Override
            public boolean matches( Object argument ) {
                List<Annotation> annotationsArg = (List)argument;
                return annotationsToFindSet.containsAll(annotationsArg)
                        && annotationsArg.size() == annotationsToFindSet.size();
            }
        };
        return argThat(argumentMatcher);
    }

    private AnnotationProcessorProvider mockAnnotationProcessorProvider() {
        final AnnotationProcessorProvider annotationProcessorProvider = mock(AnnotationProcessorProvider.class);

        sizeAnnotationWithoutProcessor = mock(Size.class);
        when(annotationProcessorProvider.getValidator(sizeAnnotationWithoutProcessor)).thenReturn( null );

        // Mock annotation processors to write mock data to model writer
        minAnnotation = mock(Min.class);
        MinAnnotationProcessor minProcessor = mock(MinAnnotationProcessor.class);
        when(minProcessor.getGroups(minAnnotation)).thenReturn(new Class[]{});
        when(minProcessor.hasGroups(minAnnotation)).thenReturn(false);
        when(minProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(minAnnotation)).thenReturn( minProcessor );

        maxAnnotation = mock(Max.class);
        MaxAnnotationProcessor maxProcessor = mock(MaxAnnotationProcessor.class);
        when(maxProcessor.getGroups(maxAnnotation)).thenReturn(new Class[]{});
        when(maxProcessor.hasGroups(maxAnnotation)).thenReturn(false);
        when(maxProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(maxAnnotation)).thenReturn( maxProcessor );


        sizeAnnotation = mock(Size.class);
        SizeAnnotationProcessor sizeProcessor = mock(SizeAnnotationProcessor.class);
        when(sizeProcessor.getGroups(sizeAnnotation)).thenReturn(new Class[]{});
        when(sizeProcessor.hasGroups(sizeAnnotation)).thenReturn(false);
        when(sizeProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(sizeAnnotation)).thenReturn( sizeProcessor );


        minWithTypeNumberAnnotation = mock(Min.class);
        MinAnnotationProcessor minWithTypeNumberProcessor = mock(MinAnnotationProcessor.class);
        when(minWithTypeNumberProcessor.getGroups(minWithTypeNumberAnnotation)).thenReturn(new Class[]{});
        when(minWithTypeNumberProcessor.hasGroups(minWithTypeNumberAnnotation)).thenReturn(false);
        when(minWithTypeNumberProcessor.requiresType()).thenReturn(true);
        when(minWithTypeNumberProcessor.requiredType()).thenReturn("number");
        when(annotationProcessorProvider.getValidator(minWithTypeNumberAnnotation)).thenReturn( minWithTypeNumberProcessor );


        maxWithTypeDecimalAnnotation = mock(Max.class);
        MaxAnnotationProcessor maxWithTypeDecimalProcessor = mock(MaxAnnotationProcessor.class);
        when(maxWithTypeDecimalProcessor.getGroups(maxWithTypeDecimalAnnotation)).thenReturn(new Class[]{});
        when(maxWithTypeDecimalProcessor.hasGroups(maxWithTypeDecimalAnnotation)).thenReturn(false);
        when(maxWithTypeDecimalProcessor.requiresType()).thenReturn(true);
        when(maxWithTypeDecimalProcessor.requiredType()).thenReturn("decimal");
        when(annotationProcessorProvider.getValidator(maxWithTypeDecimalAnnotation)).thenReturn( maxWithTypeDecimalProcessor );


        sizeWithOneGroupAnnotation = mock(Size.class);
        SizeAnnotationProcessor sizeWithOneGroupProcessor = mock(SizeAnnotationProcessor.class);
        when(sizeWithOneGroupProcessor.getGroups(sizeWithOneGroupAnnotation)).thenReturn(new Class[]{TestGroup1.class});
        when(sizeWithOneGroupProcessor.hasGroups(sizeWithOneGroupAnnotation)).thenReturn(true);
        when(sizeWithOneGroupProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(sizeWithOneGroupAnnotation)).thenReturn( sizeWithOneGroupProcessor );
        when(sizeWithOneGroupAnnotation.groups()).thenReturn(new Class[]{TestGroup1.class});


        patternWithTwoGroupsAnnotation = mock(Pattern.class);
        PatternAnnotationProcessor patternWithTwoGroupsProcessor = mock(PatternAnnotationProcessor.class);
        when(patternWithTwoGroupsProcessor.getGroups(patternWithTwoGroupsAnnotation)).thenReturn(new Class[]{TestGroup1.class, TestGroup2.class});
        when(patternWithTwoGroupsProcessor.hasGroups(patternWithTwoGroupsAnnotation)).thenReturn(true);
        when(patternWithTwoGroupsProcessor.requiresType()).thenReturn(false);
        when(annotationProcessorProvider.getValidator(patternWithTwoGroupsAnnotation)).thenReturn( patternWithTwoGroupsProcessor );
        when(patternWithTwoGroupsAnnotation.groups()).thenReturn(new Class[]{TestGroup1.class, TestGroup2.class});

        return annotationProcessorProvider;
    }

    public static class TestGroup1 {};
    public static class TestGroup2 {};
    public static class TestGroup3 {};


    private static List<Annotation> toList(Annotation...classes) {
        List<Annotation> list = new ArrayList<Annotation>(classes.length);
        for( Annotation c : classes ) {
            list.add(c);
        }
        return list;
    }
}
