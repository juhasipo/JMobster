package fi.vincit.jmobster.processor.defaults;

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

import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.AnnotationProcessorProvider;
import fi.vincit.jmobster.processor.FieldValueConverter;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class DefaultModelGeneratorTest {
    public static class TestClass1 {
    }
    public static class TestClass2 {
    }

    @Test
    public void testProcessWithoutClasses() throws Exception {
        ModelProcessor mp = mock(ModelProcessor.class);
        FieldValueConverter fvc = mock(FieldValueConverter.class);
        AnnotationProcessorProvider app = mock(AnnotationProcessorProvider.class);
        DefaultModelGenerator dmg = new DefaultModelGenerator(mp, fvc, app);

        dmg.process();

        InOrder mpInOrder = inOrder(mp);
        mpInOrder.verify(mp, times(1)).startProcessing();
        mpInOrder.verify(mp, times(1)).endProcessing();
        verify(mp, never()).processModel(any(Model.class), anyBoolean());
    }

    @Test
    public void testProcess() throws Exception {
        ModelProcessor mp = mock(ModelProcessor.class);
        FieldValueConverter fvc = mock(FieldValueConverter.class);
        AnnotationProcessorProvider app = mock(AnnotationProcessorProvider.class);
        DefaultModelGenerator dmg = new DefaultModelGenerator(mp, fvc, app);

        Class testClass = TestClass1.class;
        Class testClass2 = TestClass2.class;
        dmg.process(testClass, testClass2);

        InOrder mpInOrder = inOrder(mp);
        ArgumentCaptor<Model> firstModelCaptor = ArgumentCaptor.forClass(Model.class);
        ArgumentCaptor<Model> secondModelCaptor = ArgumentCaptor.forClass(Model.class);
        mpInOrder.verify(mp, times(1)).startProcessing();
        mpInOrder.verify(mp, times(1)).processModel(firstModelCaptor.capture(), eq(false));
        mpInOrder.verify(mp, times(1)).processModel(secondModelCaptor.capture(), eq(true));
        mpInOrder.verify(mp, times(1)).endProcessing();

        assertEquals(TestClass1.class.getName(), firstModelCaptor.getValue().getModelClass().getName());
        assertEquals(TestClass2.class.getName(), secondModelCaptor.getValue().getModelClass().getName());
    }
}
