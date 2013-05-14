package fi.vincit.jmobster.processor.defaults;

/*
 * Copyright 2012-2013 Juha Siponen
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

import fi.vincit.jmobster.ModelGenerator;
import fi.vincit.jmobster.processor.ModelProcessor;
import fi.vincit.jmobster.processor.languages.LanguageContext;
import fi.vincit.jmobster.processor.model.Model;
import fi.vincit.jmobster.util.test.TestUtil;
import fi.vincit.jmobster.util.itemprocessor.ItemStatus;
import fi.vincit.jmobster.util.itemprocessor.ItemStatuses;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultModelGeneratorTest {
    public static class TestClass1 {
    }
    public static class TestClass2 {
    }

    @Test
    public void testProcessWithoutClasses() throws Exception {
        // TODO: Rewrite
        /*
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        modelGenerator.processAll(new ArrayList<Model>());

        InOrder mpInOrder = inOrder(modelProcessor);
        mpInOrder.verify(modelProcessor, times(1)).doStartProcessing(ItemStatuses.firstAndLast());
        mpInOrder.verify(modelProcessor, times(1)).doEndProcessing(ItemStatuses.firstAndLast());
        verify( modelProcessor, never() ).doProcessModel(any(Model.class), any(ItemStatus.class));
        */
    }

    @Test
    public void testProcessWithList() throws Exception {
        // TODO: Rewrite
        /*
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        Model testModel1 = mock(Model.class);
        Model testModel2 = mock( Model.class );
        modelGenerator.processAll( TestUtil.collectionFromObjects( testModel1, testModel2 ) );

        InOrder mpInOrder = inOrder( modelProcessor );
        mpInOrder.verify(modelProcessor, times(1)).doStartProcessing(ItemStatuses.firstAndLast());
        mpInOrder.verify(modelProcessor, times(2)).doProcessModel(any(Model.class), any(ItemStatus.class));
        mpInOrder.verify(modelProcessor, times(1)).doEndProcessing(ItemStatuses.firstAndLast());
        */
    }

    @Test
    public void testProcessOne() throws Exception {
        // TODO: Rewrite
        /*
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        Model testModel1 = mock(Model.class);
        modelGenerator.process( testModel1 );

        InOrder mpInOrder = inOrder( modelProcessor );
        mpInOrder.verify(modelProcessor).doStartProcessing(ItemStatuses.firstAndLast());
        mpInOrder.verify(modelProcessor).doProcessModel(eq(testModel1), eq(ItemStatuses.firstAndLast()));
        mpInOrder.verify(modelProcessor).doEndProcessing(ItemStatuses.firstAndLast());
        */
    }

    @Test
    public void testSetLanguageContext() throws Exception {
        // TODO: Rewrite
        /*
        ModelProcessor modelProcessor = mock(ModelProcessor.class);
        ModelGenerator modelGenerator = new ModelGeneratorBuilder().setModelProcessor(modelProcessor).build();

        LanguageContext context = mock(LanguageContext.class);
        modelGenerator.setLanguageContext(context);

        verify(modelProcessor).setLanguageContext(any(LanguageContext.class));
        */
    }
}
