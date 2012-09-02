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

import fi.vincit.jmobster.processor.*;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.ItemStatus;
import fi.vincit.jmobster.util.TestUtil;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

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
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        FieldValueConverter valueConverter = mock(FieldValueConverter.class);
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        ModelFieldFactory modelFieldFactory = new DefaultModelFieldFactory( FieldScanMode.DIRECT_FIELD_ACCESS, valueConverter, validatorScanner);
        ModelNamingStrategy modelNamingStrategy = mock(ModelNamingStrategy.class);
        DefaultModelGenerator dmg = new ModelGeneratorBuilder().setModelProcessor( modelProcessor ).setModelFieldFactory( modelFieldFactory ).setModelNamingStrategy( modelNamingStrategy ).createDefaultModelGenerator();

        dmg.process();

        InOrder mpInOrder = inOrder(modelProcessor);
        mpInOrder.verify(modelProcessor, times(1)).startProcessing();
        mpInOrder.verify(modelProcessor, times(1)).endProcessing();
        verify(modelProcessor, never()).processModel(any(Model.class), any( ItemStatus.class ));
    }

    @Test
    public void testProcessVarArgs() throws Exception {
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        FieldValueConverter valueConverter = mock(FieldValueConverter.class);
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        ModelFieldFactory modelFieldFactory = new DefaultModelFieldFactory( FieldScanMode.DIRECT_FIELD_ACCESS, valueConverter, validatorScanner);
        ModelNamingStrategy modelNamingStrategy = mock(ModelNamingStrategy.class);
        DefaultModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor( modelProcessor ).setModelFieldFactory( modelFieldFactory ).setModelNamingStrategy( modelNamingStrategy ).createDefaultModelGenerator();

        Class testClass = TestClass1.class;
        Class testClass2 = TestClass2.class;
        modelGenerator.process( testClass, testClass2 );

        InOrder mpInOrder = inOrder(modelProcessor);
        mpInOrder.verify(modelProcessor, times(1)).startProcessing();
        mpInOrder.verify(modelProcessor, times(2)).processModel(any(Model.class), any(ItemStatus.class));
        mpInOrder.verify(modelProcessor, times(1)).endProcessing();
    }

    @Test
    public void testProcessWithList() throws Exception {
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        FieldValueConverter valueConverter = mock(FieldValueConverter.class);
        ValidatorScanner validatorScanner = mock(ValidatorScanner.class);
        ModelFieldFactory modelFieldFactory = new DefaultModelFieldFactory( FieldScanMode.DIRECT_FIELD_ACCESS, valueConverter, validatorScanner);
        ModelNamingStrategy modelNamingStrategy = mock(ModelNamingStrategy.class);
        DefaultModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor( modelProcessor ).setModelFieldFactory( modelFieldFactory ).setModelNamingStrategy( modelNamingStrategy ).createDefaultModelGenerator();

        Class testClass = TestClass1.class;
        Class testClass2 = TestClass2.class;
        List<Class> testClasses = TestUtil.listFromObjects(testClass, testClass2);
        modelGenerator.process( testClasses );

        InOrder mpInOrder = inOrder( modelProcessor );
        mpInOrder.verify(modelProcessor, times(1)).startProcessing();
        mpInOrder.verify(modelProcessor, times(2)).processModel( any( Model.class ), any( ItemStatus.class ) );
        mpInOrder.verify(modelProcessor, times(1)).endProcessing();
    }
}
